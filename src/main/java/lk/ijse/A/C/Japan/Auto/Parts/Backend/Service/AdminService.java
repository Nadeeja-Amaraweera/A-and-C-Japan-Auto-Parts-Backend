package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AdminDashboardDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO;

import java.util.List;

public interface AdminService {
    AdminDashboardDTO getDashboardStats();
    List<SupplierDTO> getPendingSuppliers();
    SupplierDTO approveSupplier(Long supplierId);
    SupplierDTO rejectSupplier(Long supplierId);
    org.springframework.core.io.Resource getSupplierBusinessDocument(Long supplierId);
    List<AuctionDTO> getPendingAuctions();
    AuctionDTO approveAuction(Long auctionId);
    AuctionDTO rejectAuction(Long auctionId);
    List<lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO> getAllUsers();
}
