package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.OrderDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.OrderItemDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.VehicleDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.OrderService;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final VehicleService vehicleService;

    @Override
    public OrderDTO createOrderFromCart(Long userId, String shippingAddress, PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot checkout an empty cart");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            BigDecimal subtotal = ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setSubtotal(totalAmount);
        order.setShippingCost(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(totalAmount);
        order.setShippingRecipientName(user.getUserName());
        order.setShippingPhone(user.getUserPhone());
        order.setShippingAddress(shippingAddress != null ? shippingAddress : user.getUserAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setProduct(ci.getProduct());
            oi.setVehicle(ci.getVehicle());
            oi.setItemType(ci.getProduct() != null ? "PART" : "VEHICLE");
            oi.setItemTitle(ci.getProduct() != null ? ci.getProduct().getProductName() : (ci.getVehicle() != null ? ci.getVehicle().getTitle() : "Item"));
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());
            oi.setTotalPrice(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            orderItems.add(orderItemRepository.save(oi));

            // Reduce stock if product
            if (ci.getProduct() != null) {
                Product prod = ci.getProduct();
                if (prod.getStockQuantity() != null && prod.getStockQuantity() >= ci.getQuantity()) {
                    prod.setStockQuantity(prod.getStockQuantity() - ci.getQuantity());
                } else {
                    prod.setStockQuantity(0);
                }
                productRepository.save(prod);
            }
        }
        savedOrder.setItems(orderItems);

        // Create Payment
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setUser(user);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        payment.setPaidAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Clear cart
        cartItemRepository.deleteByCart_CartId(cart.getCartId());

        return mapToDTO(savedOrder, payment);
    }

    @Override
    public OrderDTO createOrderForWinningBidder(Auction auction, User winner, BigDecimal winningAmount) {
        if (auction == null || winner == null) {
            throw new RuntimeException("Auction and winning bidder cannot be null");
        }

        // Strict duplicate check
        if (orderRepository.existsByAuction_AuctionId(auction.getAuctionId())) {
            return mapToDTO(orderRepository.findByAuction_AuctionId(auction.getAuctionId()).get(), null);
        }

        BigDecimal price = (winningAmount != null && winningAmount.compareTo(BigDecimal.ZERO) > 0)
                ? winningAmount
                : (auction.getCurrentBid() != null ? auction.getCurrentBid() : auction.getStartingPrice());

        Order order = new Order();
        order.setUser(winner);
        order.setAuction(auction);
        order.setOrderNumber("ORD-AUC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setSubtotal(price);
        order.setShippingCost(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(price);
        order.setShippingRecipientName(winner.getUserName());
        order.setShippingPhone(winner.getUserPhone() != null ? winner.getUserPhone() : "N/A");
        order.setShippingAddress(winner.getUserAddress() != null ? winner.getUserAddress() : "Customer Address on file");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Create OrderItem for auction vehicle
        OrderItem oi = new OrderItem();
        oi.setOrder(savedOrder);
        oi.setVehicle(auction.getVehicle());
        oi.setItemType("AUCTION_VEHICLE");
        oi.setItemTitle(auction.getTitle());
        oi.setQuantity(1);
        oi.setUnitPrice(price);
        oi.setTotalPrice(price);
        orderItemRepository.save(oi);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(oi);
        savedOrder.setItems(orderItems);

        // Create Payment record
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setUser(winner);
        payment.setAmount(price);
        payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-AUC-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        payment.setPaidAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        System.out.println("✅ Automatically created Order " + savedOrder.getOrderNumber() + " for winning bidder: " + winner.getUserEmail() + " on auction #" + auction.getAuctionId());
        return mapToDTO(savedOrder, payment);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomeException(404, "Order not found with id: " + orderId));

        // Security / User Ownership Check
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByUserName(currentUsername)
                    .or(() -> userRepository.findByUserEmail(currentUsername))
                    .orElse(null);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));

            if (!isAdmin && currentUser != null) {
                if (!order.getUser().getUserId().equals(currentUser.getUserId())) {
                    throw new CustomeException(403, "Access denied: You do not have permission to view this order");
                }
            }
        }

        return mapToDTO(order, null);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(o -> mapToDTO(o, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(o -> mapToDTO(o, null))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomeException(404, "Order not found with id: " + orderId));
        order.setOrderStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(orderRepository.save(order), null);
    }

    private OrderDTO mapToDTO(Order order, Payment payment) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getOrderId());
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        if (order.getAuction() != null) {
            dto.setAuctionId(order.getAuction().getAuctionId());
            dto.setAuctionTitle(order.getAuction().getTitle());
        }
        dto.setUserId(order.getUser().getUserId());
        dto.setUserName(order.getUser().getUserName());
        dto.setUserEmail(order.getUser().getUserEmail());
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingCost(order.getShippingCost());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setStatus(order.getOrderStatus());
        dto.setShippingRecipientName(order.getShippingRecipientName());
        dto.setShippingPhone(order.getShippingPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setOrderDate(order.getCreatedAt());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        if (payment == null) {
            payment = paymentRepository.findByOrder_OrderId(order.getOrderId()).orElse(null);
        }
        if (payment != null) {
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setPaymentStatus(payment.getPaymentStatus());
            dto.setTransactionId(payment.getTransactionId());
        }

        List<OrderItem> items = orderItemRepository.findByOrder_OrderId(order.getOrderId());
        List<OrderItemDTO> itemDTOs = items.stream().map(oi -> {
            OrderItemDTO oiDto = new OrderItemDTO();
            oiDto.setId(oi.getOrderItemId());
            oiDto.setOrderItemId(oi.getOrderItemId());
            oiDto.setItemType(oi.getItemType());
            oiDto.setItemTitle(oi.getItemTitle());
            oiDto.setProductTitle(oi.getItemTitle());
            oiDto.setQuantity(oi.getQuantity());
            oiDto.setUnitPrice(oi.getUnitPrice());
            oiDto.setTotalPrice(oi.getTotalPrice());
            oiDto.setSubtotal(oi.getTotalPrice());

            if (oi.getProduct() != null) {
                oiDto.setProductId(oi.getProduct().getProductId());
                oiDto.setPartNumber(oi.getProduct().getSku());
                if (oi.getProduct().getImages() != null && !oi.getProduct().getImages().isEmpty()) {
                    oiDto.setImageUrl(oi.getProduct().getImages().get(0).getImageUrl());
                }
            } else if (oi.getVehicle() != null) {
                oiDto.setVehicleId(oi.getVehicle().getVehicleId());
                VehicleDTO vDto = vehicleService.convertToDTO(oi.getVehicle());
                oiDto.setVehicle(vDto);
                if (vDto != null && vDto.getImages() != null && !vDto.getImages().isEmpty()) {
                    oiDto.setImageUrl(vDto.getImages().get(0));
                } else if (vDto != null && vDto.getPrimaryImage() != null) {
                    oiDto.setImageUrl(vDto.getPrimaryImage());
                }
            } else if (order.getAuction() != null && order.getAuction().getVehicle() != null) {
                oiDto.setVehicleId(order.getAuction().getVehicle().getVehicleId());
                VehicleDTO vDto = vehicleService.convertToDTO(order.getAuction().getVehicle());
                oiDto.setVehicle(vDto);
                if (vDto != null && vDto.getImages() != null && !vDto.getImages().isEmpty()) {
                    oiDto.setImageUrl(vDto.getImages().get(0));
                } else if (vDto != null && vDto.getPrimaryImage() != null) {
                    oiDto.setImageUrl(vDto.getPrimaryImage());
                }
            }
            return oiDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);
        dto.setOrderItems(itemDTOs);

        // Resolve primary vehicle for OrderDTO
        VehicleDTO primaryVehicle = null;
        for (OrderItemDTO oiDto : itemDTOs) {
            if (oiDto.getVehicle() != null) {
                primaryVehicle = oiDto.getVehicle();
                break;
            }
        }
        if (primaryVehicle == null && order.getAuction() != null && order.getAuction().getVehicle() != null) {
            primaryVehicle = vehicleService.convertToDTO(order.getAuction().getVehicle());
        }
        dto.setVehicle(primaryVehicle);

        return dto;
    }
}
