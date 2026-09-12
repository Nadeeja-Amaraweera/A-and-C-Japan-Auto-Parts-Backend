package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private Long parentId;
    private String type;
    private String description;
    private String iconClass;
    private Boolean isActive;
    private List<CategoryDTO> subCategories;
}
