package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long cartItemId;
    private Long productId;
    private ProductDTO product;
    private Long vehicleId;
    private VehicleDTO vehicle;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal itemTotal;
    private BigDecimal subtotal;
    private String name;
    private String productTitle;
    private String partNumber;
    private String sku;
    private String imageUrl;
    private LocalDateTime addedAt;
}
