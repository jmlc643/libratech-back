package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private TitleService titleService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BookService bookService;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        books = List.of(
                new Book(1, true,
                        new Title(1, "Cien Años de Soledad", Arrays.asList(new Book(), new Book())),
                        new Author(1, "Gabriel", "García Márquez", Arrays.asList(new Book(), new Book())),
                        new Category(1, "Novela", Arrays.asList(new Book(), new Book())), null),
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

    @Test
    void testAddBookWhenSendNewElements() {
        // Given
        CreateBookDTO createBook = new CreateBookDTO("La Ciudad de los Perros", "Mario", "Vargas Llosa", "Novela Peruana");

        Title title = new Title(6, "La Ciudad de los Perros", new ArrayList<>());
        Author author = new Author(6, "Mario", "Vargas Llosa", new ArrayList<>());
        Category category = new Category(6, "Novela Peruana", new ArrayList<>());

        TitleDTO titleDTO = new TitleDTO("La Ciudad de los Perros");
        AuthorDTO authorDTO = new AuthorDTO("Mario", "Vargas Llosa");
        CategoryDTO categoryDTO = new CategoryDTO("Novela Peruana");

        // When
        when(titleService.existsTitle(anyString())).thenReturn(false);
        when(titleService.createTitle(any(TitleDTO.class))).thenReturn(title);
        when(authorService.existsAuthor(anyString(), anyString())).thenReturn(false);
        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(author);
        when(categoryService.existsCategory(anyString())).thenReturn(false);
        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(category);
        when(titleService.returnTitleDTO(any(Title.class))).thenReturn(titleDTO);
        when(authorService.returnAuthorDTO(any(Author.class))).thenReturn(authorDTO);
        when(categoryService.returnCategoryDTO(any(Category.class))).thenReturn(categoryDTO);
        BookDTO result = bookService.addBook(createBook);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().isAvailable()).isEqualTo(true);

        assertThat(result.getAuthor().getAuthorLastName()).isEqualTo("Vargas Llosa");
        assertThat(result.getTitle().getTitleName()).isEqualTo("La Ciudad de los Perros");
        assertThat(result.getCategory().getCategoryName()).isEqualTo("Novela Peruana");
    }

    @Test
    void testAddBookWhenSendExistingElements() {
        // Given
        CreateBookDTO createBook = new CreateBookDTO("La Casa de los Espíritus", "Gabriel", "García Márquez", "Fantasía");

        Title title = books.get(1).getTitle();
        Author author = books.getFirst().getAuthor();
        Category category = books.get(2).getCategory();

        TitleDTO titleDTO = new TitleDTO("La Casa de los Espíritus");
        AuthorDTO authorDTO = new AuthorDTO("Gabriel", "García Márquez");
        CategoryDTO categoryDTO = new CategoryDTO("Fantasía");

        // When
        when(titleService.existsTitle(anyString())).thenReturn(true);
        when(titleService.getTitle(anyString())).thenReturn(title);
        when(authorService.existsAuthor(anyString(), anyString())).thenReturn(true);
        when(authorService.getAuthor(anyString(), anyString())).thenReturn(author);
        when(categoryService.existsCategory(anyString())).thenReturn(true);
        when(categoryService.getCategory(anyString())).thenReturn(category);
        when(titleService.returnTitleDTO(any(Title.class))).thenReturn(titleDTO);
        when(authorService.returnAuthorDTO(any(Author.class))).thenReturn(authorDTO);
        when(categoryService.returnCategoryDTO(any(Category.class))).thenReturn(categoryDTO);
        BookDTO result = bookService.addBook(createBook);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().isAvailable()).isEqualTo(true);

        assertThat(result.getAuthor().getAuthorLastName()).isEqualTo("García Márquez");
        assertThat(result.getTitle().getTitleName()).isEqualTo("La Casa de los Espíritus");
        assertThat(result.getCategory().getCategoryName()).isEqualTo("Fantasía");
    }

    @Test
    void testUpdateBook() {
        // Given
        int id = 1;
        UpdateBookDTO request = new UpdateBookDTO("Don Quijote", "Don Francisco", "Pizarro", "Cultura General");
        AuthorDTO authorDTO = new AuthorDTO("Don Francisco", "Pizarro");
        TitleDTO titleDTO = new TitleDTO("Don Quijote");
        CategoryDTO categoryDTO = new CategoryDTO("Cultura General");

        Author author = new Author(6, "Don Francisco", "Pizarro", new ArrayList<>());
        Title title = new Title(6, "Don Quijote", new ArrayList<>());
        Category category = new Category(6, "Cultura General", new ArrayList<>());

        Book bookBeforeUpdate = books.getFirst();
        Book bookAfterUpdate = new Book(1, true, title, author, category, null);

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.of(bookBeforeUpdate));
        when(titleService.updateTitle("Don Quijote", 1)).thenReturn(title);
        when(authorService.updateAuthor("Don Francisco", "Pizarro", 1)).thenReturn(author);
        when(categoryService.updateCategory("Cultura General", 1)).thenReturn(category);
        when(titleService.returnTitleDTO(any(Title.class))).thenReturn(titleDTO);
        when(authorService.returnAuthorDTO(any(Author.class))).thenReturn(authorDTO);
        when(categoryService.returnCategoryDTO(any(Category.class))).thenReturn(categoryDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(bookAfterUpdate);
        BookDTO result = bookService.updateBook(request, id);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().getCategory().getCategoryName()).isEqualTo("Cultura General");
        assertThat(bookArgumentCaptor.getValue().getTitle().getTitleName()).isEqualTo("Don Quijote");

        assertThat(result).isNotNull();
        assertThat(result.getIdBook()).isEqualTo(1);
        assertThat(result.getAuthor().getAuthorName()).isEqualTo("Don Francisco");
        assertThat(result.getAuthor().getAuthorLastName()).isEqualTo("Pizarro");
        assertThat(result.getCategory().getCategoryName()).isEqualTo("Cultura General");
        assertThat(result.getTitle().getTitleName()).isEqualTo("Don Quijote");
        assertThat(result.isAvailable()).isEqualTo(true);
    }

    @Test
    void testUpdateBookWhenTheBookNotExists() {
        // Given
        int id = 99999;

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> bookService.updateBook(new UpdateBookDTO("Title", "Name", "Last Name", "Category"), id));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El libro con ID 99999 no existe");
    }

    @Test
    void testDeleteBook() {
        // Given
        int id = 5;
        Book book = books.getLast();
        List<Book> booksAfterDelete = List.of(
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
                        new Category(3, "Distopía", null), null)
        );
        Page<Book> page = new PageImpl<>(booksAfterDelete.subList(0,4), PageRequest.of(0, 4), booksAfterDelete.size());
        List<BookDTO> booksDTOAfterDelete = List.of(
                new BookDTO(1,
                        new TitleDTO("Cien Años de Soledad"),
                        new AuthorDTO("Gabriel", "García Márquez"),
                        new CategoryDTO("Novela"), true),
                new BookDTO(2,
                        new TitleDTO("La Casa de los Espíritus"),
                        new AuthorDTO("Isabel", "Allende"),
                        new CategoryDTO("Novela"), true),
                new BookDTO(3,
                        new TitleDTO("Harry Potter y la Piedra Filosofal"),
                        new AuthorDTO("J.K.", "Rowling"),
                        new CategoryDTO("Fantasía"), false),
                new BookDTO(4,
                        new TitleDTO("1984"),
                        new AuthorDTO("George", "Orwell"),
                        new CategoryDTO("Distopía"), true)
        );

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(authorService.returnAuthorDTO(any(Author.class))).thenAnswer(invocation -> {
            Author author = invocation.getArgument(0);
            return new AuthorDTO(author.getAuthorName(), author.getAuthorLastName());
        });
        when(titleService.returnTitleDTO(any(Title.class))).thenAnswer(invocation -> {
            Title title = invocation.getArgument(0);
            return new TitleDTO(title.getTitleName());
        });
        when(categoryService.returnCategoryDTO(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return new CategoryDTO(category.getCategoryName());
        });

        List<BookDTO> result = bookService.deleteBook(id);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).delete(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().getIdBook()).isEqualTo(id);

        assertThat(result).hasSize(booksDTOAfterDelete.size());
        assertThat(result.getFirst().getIdBook()).isEqualTo(1);
        assertThat(result.getFirst().getTitle().getTitleName()).isEqualTo("Cien Años de Soledad");
        assertThat(result.get(2).getAuthor().getAuthorName()).isEqualTo("J.K.");
        assertThat(result.getLast().getCategory().getCategoryName()).isEqualTo("Distopía");
    }

    @Test
    void testDeleteBookWhenBookNotExists() {
        // Given
        int id = 666;

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> bookService.deleteBook(id));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El libro con ID 666 no existe");
    }
}
