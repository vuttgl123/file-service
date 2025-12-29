package com.file_service.files.domain.model;

import com.file_service.shared.domain.AbstractValueObject;

import java.util.Objects;

public final class AssetMetadata extends AbstractValueObject {
    private static final int MAX_IMAGE_DIMENSION = 4096;
    private static final double MAX_VIDEO_DURATION_SEC = 180.0; // 3 minutes

    private final String mimeType;
    private final Long sizeBytes;
    private final Integer width;
    private final Integer height;
    private final Double durationSec;
    private final String checksum;

    private AssetMetadata(
            String mimeType,
            Long sizeBytes,
            Integer width,
            Integer height,
            Double durationSec,
            String checksum
    ) {
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.width = width;
        this.height = height;
        this.durationSec = durationSec;
        this.checksum = checksum;
        validate();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getDurationSec() {
        return durationSec;
    }

    public String getChecksum() {
        return checksum;
    }

    public boolean hasChecksum() {
        return checksum != null && !checksum.isBlank();
    }

    public boolean hasDimensions() {
        return width != null && height != null;
    }

    public boolean hasDuration() {
        return durationSec != null;
    }

    public String getFormattedSize() {
        if (sizeBytes == null) {
            return "Unknown";
        }
        if (sizeBytes < 1024) {
            return sizeBytes + " B";
        } else if (sizeBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeBytes / 1024.0);
        } else {
            return String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
        }
    }

    public void validateForType(AssetType assetType) {
        if (assetType == null) {
            throw new IllegalArgumentException("AssetType cannot be null");
        }

        if (!assetType.isMimeTypeAllowed(mimeType)) {
            throw new IllegalArgumentException(
                    String.format("MIME type '%s' is not allowed for %s", mimeType, assetType)
            );
        }

        if (!assetType.isSizeValid(sizeBytes)) {
            throw new IllegalArgumentException(
                    String.format("File size %s exceeds maximum of %s for %s",
                            getFormattedSize(),
                            formatBytes(assetType.getMaxSizeBytes()),
                            assetType)
            );
        }

        switch (assetType) {
            case IMAGE -> validateImageMetadata();
            case VIDEO -> validateVideoMetadata();
        }
    }

    private void validateImageMetadata() {
        if (hasDimensions()) {
            if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
                throw new IllegalArgumentException(
                        String.format("Image dimensions %dx%d exceed maximum of %dx%d",
                                width, height, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
                );
            }
        }
    }

    private void validateVideoMetadata() {
        if (hasDuration() && durationSec > MAX_VIDEO_DURATION_SEC) {
            throw new IllegalArgumentException(
                    String.format("Video duration %.2fs exceeds maximum of %.2fs",
                            durationSec, MAX_VIDEO_DURATION_SEC)
            );
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        } else {
            return (bytes / (1024 * 1024)) + " MB";
        }
    }

    @Override
    public void validate() {
        if (mimeType == null || mimeType.isBlank()) {
            throw new IllegalArgumentException("MIME type cannot be null or blank");
        }
        if (sizeBytes == null || sizeBytes <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        if (width != null && width <= 0) {
            throw new IllegalArgumentException("Width must be greater than 0");
        }
        if (height != null && height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        if (durationSec != null && durationSec <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
    }

    @Override
    protected boolean equalsByValue(AbstractValueObject other) {
        if (!(other instanceof AssetMetadata that)) {
            return false;
        }
        return Objects.equals(this.mimeType, that.mimeType) &&
                Objects.equals(this.sizeBytes, that.sizeBytes) &&
                Objects.equals(this.width, that.width) &&
                Objects.equals(this.height, that.height) &&
                Objects.equals(this.durationSec, that.durationSec) &&
                Objects.equals(this.checksum, that.checksum);
    }

    @Override
    protected int hashCodeByValue() {
        return Objects.hash(mimeType, sizeBytes, width, height, durationSec, checksum);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AssetMetadata{");
        sb.append("mime='").append(mimeType).append('\'');
        sb.append(", size=").append(getFormattedSize());
        if (hasDimensions()) {
            sb.append(", dimensions=").append(width).append("x").append(height);
        }
        if (hasDuration()) {
            sb.append(", duration=").append(String.format("%.2fs", durationSec));
        }
        if (hasChecksum()) {
            sb.append(", checksum=").append(checksum.substring(0, Math.min(8, checksum.length()))).append("...");
        }
        sb.append('}');
        return sb.toString();
    }

    public static class Builder {
        private String mimeType;
        private Long sizeBytes;
        private Integer width;
        private Integer height;
        private Double durationSec;
        private String checksum;

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder sizeBytes(Long sizeBytes) {
            this.sizeBytes = sizeBytes;
            return this;
        }

        public Builder width(Integer width) {
            this.width = width;
            return this;
        }

        public Builder height(Integer height) {
            this.height = height;
            return this;
        }

        public Builder dimensions(Integer width, Integer height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder durationSec(Double durationSec) {
            this.durationSec = durationSec;
            return this;
        }

        public Builder checksum(String checksum) {
            this.checksum = checksum;
            return this;
        }

        public AssetMetadata build() {
            return new AssetMetadata(
                    mimeType,
                    sizeBytes,
                    width,
                    height,
                    durationSec,
                    checksum
            );
        }
    }
}