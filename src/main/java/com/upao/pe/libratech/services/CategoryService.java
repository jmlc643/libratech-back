package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.models.Category;
import com.upao.pe.libratech.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;

    // READ

    public List<CategoryDTO> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(this::returnCategoryDTO).toList();
    }

    // CREATE

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())){
            throw new ResourceAlreadyExistsException("La categoría ya existe");
        }
        Category category = new Category(null, categoryDTO.getCategoryName(), new ArrayList<>());
        categoryRepository.save(category);
        return returnCategoryDTO(category);
    }

    private CategoryDTO returnCategoryDTO(Category category) {
        return new CategoryDTO(category.getCategoryName());
    }
}
