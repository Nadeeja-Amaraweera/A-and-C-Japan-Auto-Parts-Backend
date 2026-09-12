package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO;

import java.util.List;

public interface SupplierService {
    SupplierDTO applyBecomeSupplier(Long userId, SupplierDTO dto);
    SupplierDTO applyToBeSupplier(SupplierDTO dto);
    SupplierDTO applyToBeSupplierWithDocument(SupplierDTO dto, org.springframework.web.multipart.MultipartFile documentFile);
    SupplierDTO getSupplierStatus(Long userId);
    SupplierDTO getSupplierByUserId(Long userId);
    SupplierDTO approveSupplier(Long userId);
    SupplierDTO rejectSupplier(Long userId, String reason);
    SupplierDTO rejectSupplier(Long userId);
    List<SupplierDTO> getAllPendingSuppliers();
    List<SupplierDTO> getPendingSuppliers();
}
