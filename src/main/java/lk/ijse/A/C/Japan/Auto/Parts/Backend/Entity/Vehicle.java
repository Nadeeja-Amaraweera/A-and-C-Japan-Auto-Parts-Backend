package lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity;

import jakarta.persistence.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.ConditionType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.FuelType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.TransmissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    private ConditionType vehicleCondition = ConditionType.USED;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmission = TransmissionType.AUTOMATIC;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType = FuelType.GASOLINE;

    private String engineSize;
    private String enginePower;
    private String exteriorColor;
    private String interiorColor;
    private Integer doors = 4;
    private Integer seats = 5;
    private String drivetrain = "FWD";

    private String vin;
    private String licensePlate;
    private String locationAddress;
    private String locationCity;
    private String locationCountry = "Japan";

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status = "APPROVED"; // PENDING, APPROVED, REJECTED, ACTIVE, SOLD

    private LocalDate inspectionDate;
    private String inspectionResult;
    private Integer views = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VehicleImage> images = new ArrayList<>();
}
