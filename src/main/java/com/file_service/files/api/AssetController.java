package com.file_service.files.api;

import com.file_service.files.application.assets.confirm.ConfirmAssetCommand;
import com.file_service.files.application.assets.confirm.ConfirmAssetResult;
import com.file_service.files.application.assets.confirm.ConfirmAssetUseCase;
import com.file_service.files.application.assets.presign.PresignUploadCommand;
import com.file_service.files.application.assets.presign.PresignUploadResult;
import com.file_service.files.application.assets.presign.PresignUploadUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final PresignUploadUseCase presignUploadUseCase;
    private final ConfirmAssetUseCase confirmAssetUseCase;

    public AssetController(
            PresignUploadUseCase presignUploadUseCase,
            ConfirmAssetUseCase confirmAssetUseCase
    ) {
        this.presignUploadUseCase = presignUploadUseCase;
        this.confirmAssetUseCase = confirmAssetUseCase;
    }

    @PostMapping("/presign")
    @ResponseStatus(HttpStatus.CREATED)
    public PresignUploadResult presign(@RequestBody PresignUploadCommand command) {
        return presignUploadUseCase.execute(command);
    }


    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmAssetResult confirm(@RequestBody ConfirmAssetCommand command) {
        return confirmAssetUseCase.execute(command);
    }
}