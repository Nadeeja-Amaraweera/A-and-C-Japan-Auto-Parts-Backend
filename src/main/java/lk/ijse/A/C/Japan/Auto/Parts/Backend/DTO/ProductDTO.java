package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.QualityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name; // maps to productName for frontend
    private String productName;
    private String sku;
    private String brand;
    private String compatibleModel;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private QualityType qualityType;
    private Boolean isFeatured;
    private Boolean isActive;
    private String status;
    private String primaryImage;
    private List<String> images = new ArrayList<>();
    private List<ProductImageDTO> imageDetails = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
