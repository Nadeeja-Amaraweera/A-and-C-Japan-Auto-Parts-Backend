package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDTO {
    private Long totalUsers;
    private Long totalSuppliers;
    private Long pendingSuppliers;
    private Long totalAuctions;
    private Long activeAuctions;
    private Long pendingAuctions;
    private Long totalOrders;
    private Long totalProducts;
    private BigDecimal grossRevenue;
    private BigDecimal totalRevenue;
    private Long pendingApprovals;
    private List<SupplierDTO> pendingSuppliersList;
    private List<AuctionDTO> pendingAuctionsList;
    private List<ProductDTO> pendingProductsList;
}
