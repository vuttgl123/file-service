package com.file_service.files.application.common;

import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

public class ObjectKeyFactory {

    private final Clock clock;

    public ObjectKeyFactory(Clock clock) {
        this.clock = clock;
    }

    public String newAssetKey(String type, String ext) {
        LocalDate d = LocalDate.now(clock);
        String safeType = safe(type);
        String id = UUID.randomUUID().toString();

        String suffix = (ext == null || ext.isBlank()) ? "" : ("." + safe(ext));
        return "assets/" + safeType + "/" + d.getYear() + "/" + pad2(d.getMonthValue()) + "/" + id + suffix;
    }

    private String safe(String s) {
        return s == null ? "unknown" : s.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "");
    }

    private String pad2(int v) {
        return (v < 10) ? ("0" + v) : String.valueOf(v);
    }
}
