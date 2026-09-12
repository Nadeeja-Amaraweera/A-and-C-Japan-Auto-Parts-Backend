package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.CategoryDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Category;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.CategoryRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO saveCategory(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setCategorySlug(dto.getCategorySlug());
        category.setType(dto.getType());
        category.setDescription(dto.getDescription());
        category.setIconClass(dto.getIconClass());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new CustomeException(404, "Parent category not found"));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        return saveCategory(dto);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Category not found"));

        category.setCategoryName(dto.getCategoryName());
        category.setCategorySlug(dto.getCategorySlug());
        category.setType(dto.getType());
        category.setDescription(dto.getDescription());
        category.setIconClass(dto.getIconClass());
        if (dto.getIsActive() != null) {
            category.setIsActive(dto.getIsActive());
        }

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new CustomeException(404, "Parent category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return convertToDTO(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CustomeException(404, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Category not found"));
        return convertToDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndIsActiveTrue();
        List<CategoryDTO> tree = new ArrayList<>();
        for (Category root : rootCategories) {
            CategoryDTO dto = convertToDTO(root);
            List<Category> children = categoryRepository.findByParent_CategoryIdAndIsActiveTrue(root.getCategoryId());
            dto.setSubCategories(children.stream().map(this::convertToDTO).collect(Collectors.toList()));
            tree.add(dto);
        }
        return tree;
    }

    private CategoryDTO convertToDTO(Category cat) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(cat.getCategoryId());
        dto.setCategoryName(cat.getCategoryName());
        dto.setCategorySlug(cat.getCategorySlug());
        dto.setType(cat.getType());
        dto.setDescription(cat.getDescription());
        dto.setIconClass(cat.getIconClass());
        dto.setIsActive(cat.getIsActive());
        if (cat.getParent() != null) {
            dto.setParentId(cat.getParent().getCategoryId());
        }
        return dto;
    }
}
