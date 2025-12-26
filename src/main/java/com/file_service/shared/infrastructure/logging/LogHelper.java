package com.file_service.shared.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    private final Logger logger;

    public LogHelper(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public static LogHelper of(Class<?> clazz) {
        return new LogHelper(clazz);
    }

    public void success(String action, Object... params) {
        logger.info("Success: {} - {}", action, formatParams(params));
    }

    public void error(String action, Exception e, Object... params) {
        logger.error("Error: {} - {} - Error: {}", action, formatParams(params), e.getMessage(), e);
    }

    public void warn(String action, Object... params) {
        logger.warn("Warning: {} - {}", action, formatParams(params));
    }

    public void debug(String action, Object... params) {
        logger.debug("Debug: {} - {}", action, formatParams(params));
    }

    public void start(String action, Object... params) {
        logger.info("Starting: {} - {}", action, formatParams(params));
    }

    public void end(String action, long durationMs, Object... params) {
        logger.info("Completed: {} - Duration: {}ms - {}", action, durationMs, formatParams(params));
    }

    private String formatParams(Object... params) {
        if (params == null || params.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i += 2) {
            if (i > 0) sb.append(", ");
            if (i + 1 < params.length) {
                sb.append(params[i]).append("=").append(params[i + 1]);
            } else {
                sb.append(params[i]);
            }
        }
        return sb.toString();
    }
}