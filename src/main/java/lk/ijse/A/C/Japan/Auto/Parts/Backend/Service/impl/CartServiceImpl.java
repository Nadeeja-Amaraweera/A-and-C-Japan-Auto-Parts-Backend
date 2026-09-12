package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.CartDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.CartItemDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Cart;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.CartItem;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Product;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.CartItemRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.CartRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.ProductRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Optional<CartItem> existingItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), productId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + (quantity != null ? quantity : 1));
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity != null ? quantity : 1);
            item.setUnitPrice(product.getPrice());
            item.setAddedAt(LocalDateTime.now());
            cartItemRepository.save(item);
        }

        return mapToDTO(cartRepository.findById(cart.getCartId()).orElse(cart));
    }

    @Override
    public CartDTO updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("Item does not belong to user's cart");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return mapToDTO(cartRepository.findById(cart.getCartId()).orElse(cart));
    }

    @Override
    public CartDTO removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("Item does not belong to user's cart");
        }

        cartItemRepository.delete(item);
        return mapToDTO(cartRepository.findById(cart.getCartId()).orElse(cart));
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCart_CartId(cart.getCartId());
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUser_UserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            newCart.setUpdatedAt(LocalDateTime.now());
            return cartRepository.save(newCart);
        });
    }

    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getCartId());
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());

        List<CartItem> items = cartItemRepository.findByCart_CartId(cart.getCartId());
        List<CartItemDTO> itemDTOs = items.stream().map(item -> {
            CartItemDTO itemDto = new CartItemDTO();
            itemDto.setId(item.getCartItemId());
            itemDto.setCartItemId(item.getCartItemId());

            if (item.getProduct() != null) {
                Product p = item.getProduct();
                itemDto.setProductId(p.getProductId());
                itemDto.setProductTitle(p.getProductName());
                itemDto.setName(p.getProductName());
                itemDto.setPartNumber(p.getSku());
                itemDto.setSku(p.getSku());
                if (p.getImages() != null && !p.getImages().isEmpty()) {
                    itemDto.setImageUrl(p.getImages().get(0).getImageUrl());
                }
            }

            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);

        BigDecimal total = itemDTOs.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalAmount(total);

        return dto;
    }
}
