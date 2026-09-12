package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.OrderDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.OrderStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.PaymentMethod;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse checkout(@RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) String shippingAddress,
                                   @RequestParam(required = false) PaymentMethod paymentMethod,
                                   @RequestBody(required = false) java.util.Map<String, Object> body) {
        if (body != null) {
            if (userId == null && body.get("userId") != null) {
                userId = Long.valueOf(body.get("userId").toString());
            }
            if (shippingAddress == null && body.get("shippingAddress") != null) {
                shippingAddress = body.get("shippingAddress").toString();
            }
            if (paymentMethod == null && body.get("paymentMethod") != null) {
                try {
                    paymentMethod = PaymentMethod.valueOf(body.get("paymentMethod").toString());
                } catch (Exception ignored) {}
            }
        }
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.CREDIT_CARD;
        }
        OrderDTO order = orderService.createOrderFromCart(userId, shippingAddress, paymentMethod);
        return new CommonResponse(OPERATION_SUCCESS, order, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return new CommonResponse(OPERATION_SUCCESS, order, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getOrdersByUser(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return new CommonResponse(OPERATION_SUCCESS, orders, SUCCESS_MESSAGE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new CommonResponse(OPERATION_SUCCESS, orders, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(id, status);
        return new CommonResponse(OPERATION_SUCCESS, order, SUCCESS_MESSAGE);
    }
}
