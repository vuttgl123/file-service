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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/assets")
@Tag(name = "Assets", description = "Asset management API for file uploads to cloud storage")
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

    @Operation(
            summary = "Generate presigned upload URL",
            description = "Creates a new asset record and generates a presigned URL for direct upload to cloud storage. " +
                    "The asset will be in PENDING status until confirmed."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Presigned URL generated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PresignUploadResult.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "assetId": "550e8400-e29b-41d4-a716-446655440000",
                                      "uploadUrl": "https://account.r2.cloudflarestorage.com/bucket/assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png?signature=...",
                                      "bucket": "my-assets",
                                      "objectKey": "assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png",
                                      "requiredHeaders": {
                                        "Content-Type": "image/png"
                                      }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request (invalid type or content type)",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/presign")
    @ResponseStatus(HttpStatus.CREATED)
    public PresignUploadResult presign(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Upload request details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PresignUploadCommand.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "type": "image",
                                      "contentType": "image/png",
                                      "fileName": "photo.png"
                                    }
                                    """)
                    )
            )
            @RequestBody PresignUploadCommand command
    ) {
        return presignUploadUseCase.execute(command);
    }

    @Operation(
            summary = "Confirm asset upload",
            description = "Confirms that the file has been uploaded successfully and updates the asset with metadata. " +
                    "Transitions asset from PENDING to CONFIRMED status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Asset confirmed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmAssetResult.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "assetId": "550e8400-e29b-41d4-a716-446655440000",
                                      "status": "confirmed",
                                      "bucket": "my-assets",
                                      "objectKey": "assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation failed",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asset not found",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Asset not in PENDING status",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ConfirmAssetResult confirm(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Asset confirmation with metadata",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmAssetCommand.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "assetId": "550e8400-e29b-41d4-a716-446655440000",
                                      "bucket": "my-assets",
                                      "objectKey": "assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png",
                                      "mimeType": "image/png",
                                      "sizeBytes": 123456,
                                      "width": 1920,
                                      "height": 1080,
                                      "checksum": "sha256:abcdef123456"
                                    }
                                    """)
                    )
            )
            @RequestBody ConfirmAssetCommand command
    ) {
        return confirmAssetUseCase.execute(command);
    }

    @Operation(
            summary = "Get asset public URL",
            description = "Retrieves the public URL and metadata for a confirmed asset. " +
                    "Only works for assets in CONFIRMED status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Asset URL retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAssetUrlResult.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "assetId": "550e8400-e29b-41d4-a716-446655440000",
                                      "publicUrl": "https://cdn.example.com/assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png",
                                      "bucket": "my-assets",
                                      "objectKey": "assets/image/2024/12/550e8400-e29b-41d4-a716-446655440000.png",
                                      "type": "image",
                                      "mimeType": "image/png",
                                      "sizeBytes": 123456
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Asset not confirmed",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asset not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{assetId}/url")
    @ResponseStatus(HttpStatus.OK)
    public GetAssetUrlResult getAssetUrl(
            @Parameter(description = "Asset ID (UUID)", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String assetId
    ) {
        return getAssetUrlUseCase.execute(new GetAssetUrlQuery(assetId));
    }

    @Operation(
            summary = "Delete asset",
            description = "Deletes an asset and its associated file from cloud storage. " +
                    "This operation cannot be undone."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Asset deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asset not found",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Failed to delete asset from storage",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{assetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(
            @Parameter(description = "Asset ID (UUID)", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String assetId
    ) {
        deleteAssetUseCase.execute(new DeleteAssetCommand(assetId));
    }
}