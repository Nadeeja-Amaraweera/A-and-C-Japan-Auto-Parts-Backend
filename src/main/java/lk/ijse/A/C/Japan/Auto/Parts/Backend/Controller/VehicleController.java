package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.VehicleDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/vehicles")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.FileStorageService fileStorageService;

    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse uploadImages(
            @RequestParam(value = "files", required = false) List<org.springframework.web.multipart.MultipartFile> files,
            @RequestParam(value = "images", required = false) List<org.springframework.web.multipart.MultipartFile> images) {
        List<org.springframework.web.multipart.MultipartFile> target = (files != null && !files.isEmpty()) ? files : images;
        if (target == null || target.isEmpty()) {
            return new CommonResponse(OPERATION_SUCCESS, java.util.Collections.emptyList(), "No images provided");
        }
        List<String> imageUrls = fileStorageService.storeFiles(target);
        return new CommonResponse(OPERATION_SUCCESS, imageUrls, "Images uploaded successfully");
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse uploadVehicleImages(
            @PathVariable Long id,
            @RequestParam(value = "files", required = false) List<org.springframework.web.multipart.MultipartFile> files,
            @RequestParam(value = "images", required = false) List<org.springframework.web.multipart.MultipartFile> images) {
        List<org.springframework.web.multipart.MultipartFile> target = (files != null && !files.isEmpty()) ? files : images;
        if (target == null || target.isEmpty()) {
            return new CommonResponse(OPERATION_SUCCESS, vehicleService.getVehicleById(id), "No images provided");
        }
        List<String> imageUrls = fileStorageService.storeFiles(target);
        VehicleDTO updated = null;
        for (String url : imageUrls) {
            updated = vehicleService.addImageToVehicle(id, url, false);
        }
        if (updated == null) {
            updated = vehicleService.getVehicleById(id);
        }
        return new CommonResponse(OPERATION_SUCCESS, updated, "Vehicle images updated successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return new CommonResponse(OPERATION_SUCCESS, vehicles, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return new CommonResponse(OPERATION_SUCCESS, vehicle, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/supplier/{supplierId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getVehiclesBySupplier(@PathVariable Long supplierId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByUser(supplierId);
        return new CommonResponse(OPERATION_SUCCESS, vehicles, SUCCESS_MESSAGE);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.saveVehicle(vehicleDTO);
        return new CommonResponse(OPERATION_SUCCESS, created, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updated = vehicleService.updateVehicle(id, vehicleDTO);
        return new CommonResponse(OPERATION_SUCCESS, updated, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return new CommonResponse(OPERATION_SUCCESS, null, SUCCESS_MESSAGE);
    }
}
