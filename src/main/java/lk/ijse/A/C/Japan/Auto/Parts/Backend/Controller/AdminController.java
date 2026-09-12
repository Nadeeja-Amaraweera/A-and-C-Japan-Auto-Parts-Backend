package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AdminDashboardDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDashboard() {
        AdminDashboardDTO stats = adminService.getDashboardStats();
        return new CommonResponse(OPERATION_SUCCESS, stats, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/suppliers/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPendingSuppliers() {
        List<SupplierDTO> suppliers = adminService.getPendingSuppliers();
        return new CommonResponse(OPERATION_SUCCESS, suppliers, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/suppliers/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse approveSupplier(@PathVariable Long id) {
        SupplierDTO supplier = adminService.approveSupplier(id);
        return new CommonResponse(OPERATION_SUCCESS, supplier, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/suppliers/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse rejectSupplier(@PathVariable Long id) {
        SupplierDTO supplier = adminService.rejectSupplier(id);
        return new CommonResponse(OPERATION_SUCCESS, supplier, SUCCESS_MESSAGE);
    }

    @GetMapping("/suppliers/{id}/business-document")
    public ResponseEntity<Resource> getSupplierBusinessDocument(@PathVariable Long id, HttpServletRequest request) {
        Resource resource = adminService.getSupplierBusinessDocument(id);

        String contentType = null;
        try {
            if (resource.getFile() != null) {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            }
        } catch (Exception ex) {
            // fallback if getFile() is not accessible
        }

        if (contentType == null) {
            String filename = resource.getFilename();
            if (filename != null) {
                String lower = filename.toLowerCase();
                if (lower.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
                    contentType = "image/jpeg";
                } else if (lower.endsWith(".png")) {
                    contentType = "image/png";
                }
            }
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .body(resource);
    }


    @GetMapping(value = "/auctions/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getPendingAuctions() {
        List<AuctionDTO> auctions = adminService.getPendingAuctions();
        return new CommonResponse(OPERATION_SUCCESS, auctions, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/auctions/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse approveAuction(@PathVariable Long id) {
        AuctionDTO auction = adminService.approveAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, auction, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/auctions/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse rejectAuction(@PathVariable Long id) {
        AuctionDTO auction = adminService.rejectAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, auction, SUCCESS_MESSAGE);
    }
}
