package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Supplier;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.SupplierStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.SupplierRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Override
    public SupplierDTO applyBecomeSupplier(Long userId, SupplierDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomeException(404, "User not found"));

        Optional<Supplier> existing = supplierRepository.findByUser_UserId(userId);
        Supplier supplier;
        if (existing.isPresent()) {
            supplier = existing.get();
        } else {
            supplier = new Supplier();
            supplier.setUser(user);
        }

        String businessName = dto.getSupplierBusinessName() != null ? dto.getSupplierBusinessName() : dto.getBusinessName();
        String businessAddress = dto.getSupplierBusinessAddress() != null ? dto.getSupplierBusinessAddress() : (dto.getBusinessAddress() != null ? dto.getBusinessAddress() : (dto.getAddress() != null ? dto.getAddress() : user.getUserAddress()));
        String contactNumber = dto.getSupplierContactNumber() != null ? dto.getSupplierContactNumber() : (dto.getPhone() != null ? dto.getPhone() : user.getUserPhone());
        String contactPerson = dto.getSupplierName() != null ? dto.getSupplierName() : (dto.getContactPerson() != null ? dto.getContactPerson() : user.getUserName());

        supplier.setSupplierName(contactPerson);
        supplier.setSupplierBusinessName(businessName);
        supplier.setSupplierBusinessAddress(businessAddress);
        supplier.setSupplierContactNumber(contactNumber);
        supplier.setRegistrationDocUrl(dto.getRegistrationDocUrl() != null ? dto.getRegistrationDocUrl() : "documents/registration_doc.pdf");
        supplier.setSupplierStatus(SupplierStatus.PENDING);
        supplier.setRejectionReason(null);
        supplier.setCreatedAt(LocalDateTime.now());

        Supplier saved = supplierRepository.save(supplier);
        return convertToDTO(saved);
    }

    @Override
    public SupplierDTO getSupplierStatus(Long userId) {
        Supplier supplier = supplierRepository.findByUser_UserId(userId)
                .orElse(null);
        if (supplier == null) {
            return null;
        }
        return convertToDTO(supplier);
    }

    @Override
    public SupplierDTO getSupplierByUserId(Long userId) {
        Supplier supplier = supplierRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomeException(404, "Supplier application not found"));
        return convertToDTO(supplier);
    }

    @Override
    public SupplierDTO approveSupplier(Long userId) {
        Supplier supplier = supplierRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomeException(404, "Supplier application not found"));

        supplier.setSupplierStatus(SupplierStatus.APPROVED);
        supplier.setApprovedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        // Update user role to SUPPLIER
        User user = supplier.getUser();
        user.setUserRole(Role.SUPPLIER);
        userRepository.save(user);

        return convertToDTO(supplier);
    }

    @Override
    public SupplierDTO rejectSupplier(Long userId, String reason) {
        Supplier supplier = supplierRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomeException(404, "Supplier application not found"));

        supplier.setSupplierStatus(SupplierStatus.REJECTED);
        supplier.setRejectionReason(reason);
        supplierRepository.save(supplier);
        return convertToDTO(supplier);
    }

    @Override
    public SupplierDTO applyToBeSupplier(SupplierDTO dto) {
        return applyBecomeSupplier(dto.getUserId(), dto);
    }

    @Override
    public SupplierDTO rejectSupplier(Long userId) {
        return rejectSupplier(userId, "Rejected by administrator");
    }

    @Override
    public List<SupplierDTO> getAllPendingSuppliers() {
        return supplierRepository.findBySupplierStatus(SupplierStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierDTO> getPendingSuppliers() {
        return getAllPendingSuppliers();
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setSupplierId(supplier.getSupplierId());
        if (supplier.getUser() != null) {
            dto.setUserId(supplier.getUser().getUserId());
            dto.setUserEmail(supplier.getUser().getUserEmail());
        }
        dto.setSupplierName(supplier.getSupplierName());
        dto.setSupplierBusinessName(supplier.getSupplierBusinessName());
        dto.setSupplierBusinessAddress(supplier.getSupplierBusinessAddress());
        dto.setSupplierContactNumber(supplier.getSupplierContactNumber());
        dto.setRegistrationDocUrl(supplier.getRegistrationDocUrl());
        dto.setSupplierStatus(supplier.getSupplierStatus());
        dto.setRejectionReason(supplier.getRejectionReason());
        dto.setApprovedAt(supplier.getApprovedAt());
        dto.setCreatedAt(supplier.getCreatedAt());
        return dto;
    }
}
