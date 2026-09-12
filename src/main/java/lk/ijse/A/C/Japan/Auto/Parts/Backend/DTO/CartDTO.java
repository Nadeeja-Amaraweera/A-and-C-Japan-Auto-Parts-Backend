package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

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
public class CartDTO {
    private Long id;
    private Long cartId;
    private Long userId;
    private Integer totalItems;
    private BigDecimal subtotal;
    private BigDecimal totalAmount;
    private List<CartItemDTO> items = new ArrayList<>();
    private LocalDateTime updatedAt;
}
