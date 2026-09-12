package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.ProductDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.ProductImageDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Category;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Product;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.ProductImage;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.QualityType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.CategoryRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.ProductImageRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.ProductRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId).stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        Product product = new Product();
        String name = dto.getName() != null ? dto.getName() : dto.getProductName();
        product.setProductName(name != null ? name : "Auto Part");
        product.setSku(dto.getSku() != null ? dto.getSku() : "SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        product.setBrand(dto.getBrand() != null ? dto.getBrand() : "OEM");
        product.setCompatibleModel(dto.getCompatibleModel());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 0);
        product.setQualityType(dto.getQualityType() != null ? dto.getQualityType() : QualityType.BRAND_NEW);
        product.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false);
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        product.setStatus("APPROVED");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }

        User supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = userRepository.findById(dto.getSupplierId()).orElse(null);
        }
        if (supplier == null) {
            List<User> users = userRepository.findAll();
            if (!users.isEmpty()) supplier = users.get(0);
        }
        product.setSupplier(supplier);

        Product savedProduct = productRepository.save(product);

        if (dto.getPrimaryImage() != null && !dto.getPrimaryImage().isEmpty()) {
            ProductImage img = new ProductImage();
            img.setProduct(savedProduct);
            img.setImageUrl(dto.getPrimaryImage());
            img.setIsPrimary(true);
            productImageRepository.save(img);
        }

        return mapToDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Product not found with id: " + id));

        if (dto.getName() != null) product.setProductName(dto.getName());
        if (dto.getProductName() != null) product.setProductName(dto.getProductName());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getCompatibleModel() != null) product.setCompatibleModel(dto.getCompatibleModel());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getStockQuantity() != null) product.setStockQuantity(dto.getStockQuantity());
        if (dto.getQualityType() != null) product.setQualityType(dto.getQualityType());
        if (dto.getIsActive() != null) product.setIsActive(dto.getIsActive());
        if (dto.getIsFeatured() != null) product.setIsFeatured(dto.getIsFeatured());
        product.setUpdatedAt(LocalDateTime.now());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }

        return mapToDTO(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Product not found with id: " + id));
        product.setIsActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return productRepository.findByIsActiveTrue().stream()
                .filter(p -> (p.getProductName() != null && p.getProductName().toLowerCase().contains(lowerKeyword))
                        || (p.getBrand() != null && p.getBrand().toLowerCase().contains(lowerKeyword))
                        || (p.getSku() != null && p.getSku().toLowerCase().contains(lowerKeyword))
                        || (p.getCompatibleModel() != null && p.getCompatibleModel().toLowerCase().contains(lowerKeyword)))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getProductId());
        dto.setName(product.getProductName());
        dto.setProductName(product.getProductName());
        dto.setSku(product.getSku());
        dto.setBrand(product.getBrand());
        dto.setCompatibleModel(product.getCompatibleModel());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setQualityType(product.getQualityType());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsActive(product.getIsActive());
        dto.setStatus(product.getStatus());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryId());
            dto.setCategoryName(product.getCategory().getCategoryName());
        }

        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getUserId());
            dto.setSupplierName(product.getSupplier().getUserName());
        }

        List<ProductImage> imgs = productImageRepository.findByProduct_ProductId(product.getProductId());
        if (imgs != null && !imgs.isEmpty()) {
            dto.setPrimaryImage(imgs.get(0).getImageUrl());
            dto.setImages(imgs.stream().map(ProductImage::getImageUrl).collect(Collectors.toList()));
            dto.setImageDetails(imgs.stream().map(img -> {
                ProductImageDTO imgDto = new ProductImageDTO();
                imgDto.setId(img.getImageId());
                imgDto.setImageUrl(img.getImageUrl());
                imgDto.setIsPrimary(img.getIsPrimary());
                return imgDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
}
