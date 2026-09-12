package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.CartDTO;

public interface CartService {
    CartDTO getCartByUserId(Long userId);
    CartDTO addItemToCart(Long userId, Long productId, Integer quantity);
    CartDTO updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity);
    CartDTO removeItemFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
}
