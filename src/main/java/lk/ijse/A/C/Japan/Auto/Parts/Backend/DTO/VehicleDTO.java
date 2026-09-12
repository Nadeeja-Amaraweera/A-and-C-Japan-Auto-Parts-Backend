package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    private Long id;
    private Long userId;
    private String sellerName;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String brand;
    private String make; // compatibility alias for frontend
    private String model;
    private Integer year;
    private Integer mileage;
    private BigDecimal price;
    private String currency;
    private ConditionType condition;
    private TransmissionType transmission;
    private FuelType fuelType;
    private String engineSize;
    private String enginePower;
    private String color;
    private String interiorColor;
    private Integer doors;
    private Integer seats;
    private String drivetrain;
    private String vin;
    private String licensePlate;
    private String locationAddress;
    private String locationCity;
    private String locationCountry;
    private String description;
    private String status;
    private LocalDate inspectionDate;
    private String inspectionResult;
    private Integer views;
    private String primaryImage;
    private List<String> images = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private List<VehicleImageDTO> imageDetails = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
