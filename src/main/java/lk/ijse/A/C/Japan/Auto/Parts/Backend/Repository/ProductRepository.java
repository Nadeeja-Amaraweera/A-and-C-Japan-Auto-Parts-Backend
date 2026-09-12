package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findBySku(String sku);
    List<Product> findBySupplier_UserId(Long supplierId);
    List<Product> findByCategory_CategoryId(Long categoryId);
    List<Product> findByIsActiveTrue();
    List<Product> findByIsFeaturedTrueAndIsActiveTrue();
    List<Product> findByPriceLessThanEqualAndIsActiveTrue(BigDecimal price);
    List<Product> findByStatus(String status);
}
