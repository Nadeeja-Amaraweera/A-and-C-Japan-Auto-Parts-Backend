package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.OrderStatus;
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
public class OrderDTO {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private Long auctionId;
    private String auctionTitle;
    private Long userId;
    private String userName;
    private String userEmail;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String promoCode;
    private OrderStatus orderStatus;
    private OrderStatus status;
    private String shippingRecipientName;
    private String shippingPhone;
    private String shippingAddress;
    private lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.PaymentMethod paymentMethod;
    private lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.PaymentStatus paymentStatus;
    private String transactionId;
    private List<OrderItemDTO> items = new ArrayList<>();
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
