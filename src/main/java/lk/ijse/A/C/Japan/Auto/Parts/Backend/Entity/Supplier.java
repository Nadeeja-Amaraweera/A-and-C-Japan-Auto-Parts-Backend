package lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity;

import jakarta.persistence.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String supplierName;
    private String supplierBusinessName;
    private String supplierBusinessAddress;
    private String supplierContactNumber;
    private String registrationDocUrl;

    @Column(name = "business_registration_document")
    private String businessRegistrationDocument;

    @Enumerated(EnumType.STRING)
    private SupplierStatus supplierStatus = SupplierStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private LocalDateTime approvedAt;
    private LocalDateTime createdAt = LocalDateTime.now();
}