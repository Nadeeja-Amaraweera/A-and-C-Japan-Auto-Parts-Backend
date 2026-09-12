package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.OrderDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.OrderStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.PaymentMethod;

import java.util.List;

public interface OrderService {
    OrderDTO createOrderFromCart(Long userId, String shippingAddress, PaymentMethod paymentMethod);
    OrderDTO createOrderForWinningBidder(lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Auction auction, lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User winner, java.math.BigDecimal winningAmount);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getOrdersByUserId(Long userId);
    List<OrderDTO> getAllOrders();
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
}
