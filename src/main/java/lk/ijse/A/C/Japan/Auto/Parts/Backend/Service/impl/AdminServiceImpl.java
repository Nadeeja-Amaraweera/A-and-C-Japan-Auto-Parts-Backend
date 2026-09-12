package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AdminDashboardDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Order;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Supplier;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.AuctionStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AdminService;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AuctionService;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final AuctionRepository auctionRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final AuctionService auctionService;

    @Override
    public AdminDashboardDTO getDashboardStats() {
        AdminDashboardDTO stats = new AdminDashboardDTO();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalSuppliers(supplierRepository.count());
        List<SupplierDTO> pendingSuppliers = supplierService.getAllPendingSuppliers();
        stats.setPendingSuppliers((long) pendingSuppliers.size());
        stats.setPendingSuppliersList(pendingSuppliers);

        stats.setTotalAuctions(auctionRepository.count());
        List<AuctionDTO> activeAuctions = auctionService.getActiveAuctions();
        stats.setActiveAuctions((long) activeAuctions.size());

        List<AuctionDTO> allAuctions = auctionService.getAllAuctions();
        List<AuctionDTO> pendingAuctions = allAuctions.stream()
                .filter(a -> (a.getStatus() == AuctionStatus.PENDING || Boolean.FALSE.equals(a.getIsApproved())))
                .collect(Collectors.toList());
        stats.setPendingAuctions((long) pendingAuctions.size());
        stats.setPendingAuctionsList(pendingAuctions);

        stats.setPendingApprovals(stats.getPendingSuppliers() + stats.getPendingAuctions());

        stats.setTotalOrders(orderRepository.count());
        stats.setTotalProducts(productRepository.count());

        List<Order> orders = orderRepository.findAll();
        BigDecimal totalRev = orders.stream()
                .map(Order::getTotalAmount)
                .filter(a -> a != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(totalRev);
        stats.setGrossRevenue(totalRev);

        return stats;
    }

    @Override
    public List<SupplierDTO> getPendingSuppliers() {
        return supplierService.getAllPendingSuppliers();
    }

    @Override
    public SupplierDTO approveSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElse(null);
        Long userId = supplier != null && supplier.getUser() != null ? supplier.getUser().getUserId() : supplierId;
        return supplierService.approveSupplier(userId);
    }

    @Override
    public SupplierDTO rejectSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElse(null);
        Long userId = supplier != null && supplier.getUser() != null ? supplier.getUser().getUserId() : supplierId;
        return supplierService.rejectSupplier(userId, "Application rejected by administrator");
    }

    @Override
    public List<AuctionDTO> getPendingAuctions() {
        return auctionService.getAllAuctions().stream()
                .filter(a -> a.getStatus() == AuctionStatus.PENDING || Boolean.FALSE.equals(a.getIsApproved()))
                .collect(Collectors.toList());
    }

    @Override
    public AuctionDTO approveAuction(Long auctionId) {
        return auctionService.approveAuction(auctionId);
    }

    @Override
    public AuctionDTO rejectAuction(Long auctionId) {
        return auctionService.rejectAuction(auctionId);
    }
}
