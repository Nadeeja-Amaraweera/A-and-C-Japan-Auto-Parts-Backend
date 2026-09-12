package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(Long categoryId);
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    List<ProductDTO> searchProducts(String keyword);
}
