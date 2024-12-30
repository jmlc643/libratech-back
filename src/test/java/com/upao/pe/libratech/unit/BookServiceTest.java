package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.models.Author;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.models.Category;
import com.upao.pe.libratech.models.Title;
import com.upao.pe.libratech.repos.BookRepository;
import com.upao.pe.libratech.services.AuthorService;
import com.upao.pe.libratech.services.BookService;
import com.upao.pe.libratech.services.CategoryService;
import com.upao.pe.libratech.services.TitleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorService authorService;

    @Mock
    TitleService titleService;

    @Mock
    CategoryService categoryService;

    @InjectMocks
    BookService bookService;

    List<Book> books;

    @BeforeEach
    void setUp() {
        books = List.of(
                new Book(1, true,
                        new Title(1, "Cien Años de Soledad", null),
                        new Author(1, "Gabriel", "García Márquez", null),
                        new Category(1, "Novela", null), null),
                new Book(2, true,
                        new Title(2, "La Casa de los Espíritus", null),
                        new Author(2, "Isabel", "Allende", null),
                        new Category(1, "Novela", null), null),
                new Book(3, false,
                        new Title(3, "Harry Potter y la Piedra Filosofal", null),
                        new Author(3, "J.K.", "Rowling", null),
                        new Category(2, "Fantasía", null), null),
                new Book(4, true,
                        new Title(4, "1984", null),
                        new Author(4, "George", "Orwell", null),
                        new Category(3, "Distopía", null), null),
                new Book(5, true,
                        new Title(5, "Matar a un Ruiseñor", null),
                        new Author(5, "Harper", "Lee", null),
                        new Category(1, "Novela", null), null)
        );
    }

    @Test
    void testFindAll() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Book> page = new PageImpl<>(books.subList(0, 3), pageable, books.size());

        TitleDTO titleDTO = new TitleDTO("La Casa de los Espíritus");
        AuthorDTO authorDTO = new AuthorDTO("Gabriel", "García Márquez");
        CategoryDTO categoryDTO = new CategoryDTO("Fantasía");

        // When
        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(titleService.returnTitleDTO(any(Title.class))).thenReturn(titleDTO);
        when(authorService.returnAuthorDTO(any(Author.class))).thenReturn(authorDTO);
        when(categoryService.returnCategoryDTO(any(Category.class))).thenReturn(categoryDTO);

        List<BookDTO> result = bookService.findAll(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdBook()).isEqualTo(1);
        assertThat(result.getLast().getIdBook()).isEqualTo(3);
        verify(bookRepository).findAll(pageable);

        assertThat(result.get(0).getAuthor().getAuthorName()).isEqualTo("Gabriel");
        assertThat(result.get(1).getTitle().getTitleName()).isEqualTo("La Casa de los Espíritus");
        assertThat(result.get(2).getCategory().getCategoryName()).isEqualTo("Fantasía");
    }
    /*

    TESTEAR LOS METODOS UPDATE DE LOS SERVICIOS AUTHOR, TITLE Y CATEGORY

    */
}
