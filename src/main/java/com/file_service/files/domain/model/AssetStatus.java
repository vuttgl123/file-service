package com.file_service.files.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum AssetStatus {
    PENDING("pending"),

    CONFIRMED("confirmed"),

    FAILED("failed");

    private final String value;

    AssetStatus(String value) {
        this.value = value;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isConfirmed() {
        return this == CONFIRMED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean canTransitionTo(AssetStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == CONFIRMED || newStatus == FAILED;
            case CONFIRMED, FAILED -> false;
        };
    }

    public void validateTransition(AssetStatus newStatus) {
        if (!canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s", this, newStatus)
            );
        }
    }

    public static AssetStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AssetStatus value cannot be null or blank");
        }
        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid AssetStatus: " + value + ". Allowed values: " +
                                Arrays.stream(values())
                                        .map(AssetStatus::getValue)
                                        .collect(Collectors.joining(", "))
                ));
    }
}