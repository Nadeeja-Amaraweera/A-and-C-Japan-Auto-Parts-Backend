package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategorySlug(String categorySlug);
    List<Category> findByParentIsNullAndIsActiveTrue();
    List<Category> findByParent_CategoryIdAndIsActiveTrue(Long parentId);
    List<Category> findByTypeAndIsActiveTrue(String type);
}
