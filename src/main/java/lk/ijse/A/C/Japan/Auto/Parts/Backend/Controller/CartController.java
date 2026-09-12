package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.CartDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCart(@RequestParam Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return new CommonResponse(OPERATION_SUCCESS, cart, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addItemToCart(@RequestParam(required = false) Long userId,
                                        @RequestParam(required = false) Long productId,
                                        @RequestParam(required = false, defaultValue = "1") Integer quantity,
                                        @RequestBody(required = false) java.util.Map<String, Object> body) {
        if (body != null) {
            if (userId == null && body.get("userId") != null) {
                userId = Long.valueOf(body.get("userId").toString());
            }
            if (productId == null && body.get("productId") != null) {
                productId = Long.valueOf(body.get("productId").toString());
            }
            if (body.get("quantity") != null) {
                quantity = Integer.valueOf(body.get("quantity").toString());
            }
        }
        CartDTO cart = cartService.addItemToCart(userId, productId, quantity != null ? quantity : 1);
        return new CommonResponse(OPERATION_SUCCESS, cart, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCartItemQuantity(@PathVariable Long itemId,
                                                 @RequestParam Long userId,
                                                 @RequestParam Integer quantity) {
        CartDTO cart = cartService.updateCartItemQuantity(userId, itemId, quantity);
        return new CommonResponse(OPERATION_SUCCESS, cart, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/items/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse removeItemFromCart(@PathVariable Long itemId,
                                             @RequestParam Long userId) {
        CartDTO cart = cartService.removeItemFromCart(userId, itemId);
        return new CommonResponse(OPERATION_SUCCESS, cart, SUCCESS_MESSAGE);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return new CommonResponse(OPERATION_SUCCESS, null, SUCCESS_MESSAGE);
    }
}
