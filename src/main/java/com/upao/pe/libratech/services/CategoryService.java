package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Category;
import com.upao.pe.libratech.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;

    // READ

    public List<CategoryDTO> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream().map(this::returnCategoryDTO).toList();
    }

    // CREATE

    public Category createCategory(CategoryDTO categoryDTO) {
        if(existsCategory(categoryDTO.getCategoryName())){
            throw new ResourceAlreadyExistsException("La categoría ya existe");
        }
        Category category = new Category(null, categoryDTO.getCategoryName(), new ArrayList<>());
        return categoryRepository.save(category);
    }

    public Category updateCategory(String categoryName, Integer idCategory) {
        Category category = getCategory(idCategory);
        if(category.getBooks().size() > 1) {
            return createCategory(new CategoryDTO(categoryName));
        } else {
            category.setCategoryName(categoryName);
            return categoryRepository.save(category);
        }
    }

    public CategoryDTO returnCategoryDTO(Category category) {
        return new CategoryDTO(category.getCategoryName());
    }

    public Category getCategory(String categoryName) {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
        if(category.isEmpty()) {
            throw new ResourceNotExistsException("La categoria con nombre "+categoryName+" no existe");
        }
        return category.get();
    }

    private Category getCategory(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new ResourceNotExistsException("La categoria con ID "+id+" no existe");
        }
        return category.get();
    }

    public boolean existsCategory(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }
}
