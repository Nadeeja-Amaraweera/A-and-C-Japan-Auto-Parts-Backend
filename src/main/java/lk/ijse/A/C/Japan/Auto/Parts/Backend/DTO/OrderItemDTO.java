package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderItemId;
    private Long productId;
    private Long vehicleId;
    private String itemType;
    private String itemTitle;
    private String productTitle;
    private String partNumber;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal subtotal;
}
