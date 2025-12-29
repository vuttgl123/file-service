package com.file_service.files.application.assets.presign;

import com.file_service.shared.application.Command;

public record PresignUploadCommand(
        String type,
        String contentType,
        String fileName
) implements Command {
}