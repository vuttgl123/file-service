package com.file_service.files.api;

import com.file_service.files.application.assets.confirm.ConfirmAssetCommand;
import com.file_service.files.application.assets.confirm.ConfirmAssetResult;
import com.file_service.files.application.assets.confirm.ConfirmAssetUseCase;
import com.file_service.files.application.assets.delete.DeleteAssetCommand;
import com.file_service.files.application.assets.delete.DeleteAssetUseCase;
import com.file_service.files.application.assets.presign.PresignUploadCommand;
import com.file_service.files.application.assets.presign.PresignUploadResult;
import com.file_service.files.application.assets.presign.PresignUploadUseCase;
import com.file_service.files.application.assets.query.GetAssetUrlQuery;
import com.file_service.files.application.assets.query.GetAssetUrlResult;
import com.file_service.files.application.assets.query.GetAssetUrlUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    private final PresignUploadUseCase presignUploadUseCase;
    private final ConfirmAssetUseCase confirmAssetUseCase;
    private final GetAssetUrlUseCase getAssetUrlUseCase;
    private final DeleteAssetUseCase deleteAssetUseCase;

    public AssetController(
            PresignUploadUseCase presignUploadUseCase,
            ConfirmAssetUseCase confirmAssetUseCase,
            GetAssetUrlUseCase getAssetUrlUseCase,
            DeleteAssetUseCase deleteAssetUseCase
    ) {
        this.presignUploadUseCase = presignUploadUseCase;
        this.confirmAssetUseCase = confirmAssetUseCase;
        this.getAssetUrlUseCase = getAssetUrlUseCase;
        this.deleteAssetUseCase = deleteAssetUseCase;
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

    @GetMapping("/{assetId}/url")
    @ResponseStatus(HttpStatus.OK)
    public GetAssetUrlResult getAssetUrl(@PathVariable String assetId) {
        return getAssetUrlUseCase.execute(new GetAssetUrlQuery(assetId));
    }

    @DeleteMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(@PathVariable String assetId) {
        deleteAssetUseCase.execute(new DeleteAssetCommand(assetId));
    }

}