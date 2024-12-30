package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.models.Category;
import com.upao.pe.libratech.repos.CategoryRepository;
import com.upao.pe.libratech.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private List<Category> categories;

    @BeforeEach
    void setUp(){
        categories = List.of(
                new Category (1, "Computer Science", null),
                new Category (2, "Software Engineering", null),
                new Category (3, "Programming", null),
                new Category (4,"Design", null),
                new Category (5, "Artificial Intelligence", null)
        );
    }

    @Test
    void testFindAll(){
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Category> page = new PageImpl<>(categories.subList(0, 3), pageable, categories.size());

        // When
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        List<CategoryDTO> result = categoryService.findAll(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getCategoryName()).isEqualTo("Computer Science");
        assertThat(result.getLast().getCategoryName()).isEqualTo("Programming");
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    void testCreateCategory() {
        // Given
        CategoryDTO category = new CategoryDTO("Data Science");
        Category categorySaved = new Category(6, "Data Science", new ArrayList<>());

        // When
        when(categoryRepository.save(any(Category.class))).thenReturn(categorySaved);
        Category result = this.categoryService.createCategory(category);

        // Then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        assertEquals("Data Science", categoryArgumentCaptor.getValue().getCategoryName());

        assertThat(result).isNotNull();
        assertThat(result.getIdCategory()).isEqualTo(6);
        assertThat(result.getCategoryName()).isEqualTo("Data Science");
        assertThat(result.getBooks()).isEmpty();
    }

    @Test
    void testCreateCategoryWhenCategoryNameExists() {
        // Given
        CategoryDTO category = new CategoryDTO("Software Engineering");

        // When
        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> categoryService.createCategory(category));

        // Then
        assertThat(ex.getMessage()).isEqualTo("La categoría ya existe");
    }

    @Test
    void testReturnCategoryDTO() {
        // Given
        Category category = categories.getFirst();

        // When
        CategoryDTO result = categoryService.returnCategoryDTO(category);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCategoryName()).isEqualTo(category.getCategoryName());
    }
}
