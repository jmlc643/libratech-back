package com.upao.pe.libratech.unit.services;

import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.repos.BookRepository;
import com.upao.pe.libratech.services.BookService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        books = List.of(
                new Book(1, true, "Cien Años de Soledad", "Gabriel García Márquez", "Novela", null),
                new Book(2, true, "La Casa de los Espíritus", "Isabel Allende", "Novela", null),
                new Book(3, false, "Harry Potter y la Piedra Filosofal", "J.K. Rowling", "Fantasía", null),
                new Book(4, true, "1984", "George Orwell", "Distopía", null),
                new Book(5, true, "Matar a un Ruiseñor", "Harper Lee", "Novela", null)
        );

    }

    @Test
    void testFindAll() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Book> page = new PageImpl<>(books.subList(0, 3), pageable, books.size());

        // When
        when(bookRepository.findAll(pageable)).thenReturn(page);

        List<BookDTO> result = bookService.findAll(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getIdBook()).isEqualTo(1);
        assertThat(result.getLast().getIdBook()).isEqualTo(3);
        verify(bookRepository).findAll(pageable);

        assertThat(result.get(0).getAuthor()).isEqualTo("Gabriel García Márquez");
        assertThat(result.get(1).getTitle()).isEqualTo("La Casa de los Espíritus");
        assertThat(result.get(2).getCategory()).isEqualTo("Fantasía");
    }

    @Test
    void testAddBook() {
        // Given
        CreateBookDTO createBook = new CreateBookDTO("La Ciudad de los Perros", "Mario Vargas Llosa", "Novela Peruana");

        // When
        BookDTO result = bookService.addBook(createBook);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().isAvailable()).isEqualTo(true);

        assertThat(result.getAuthor()).isEqualTo("Mario Vargas Llosa");
        assertThat(result.getTitle()).isEqualTo("La Ciudad de los Perros");
        assertThat(result.getCategory()).isEqualTo("Novela Peruana");
    }

    @Test
    void testUpdateBook() {
        // Given
        int id = 1;
        UpdateBookDTO request = new UpdateBookDTO("Don Quijote", "Don Francisco Pizarro", "Cultura General");
        Book bookBeforeUpdate = books.getFirst();
        Book bookAfterUpdate = new Book(1, true, "Don Quijote", "Don Francisco Pizarro", "Cultura General", null);

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.of(bookBeforeUpdate));
        when(bookRepository.save(any(Book.class))).thenReturn(bookAfterUpdate);
        BookDTO result = bookService.updateBook(request, id);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().getCategory()).isEqualTo("Cultura General");
        assertThat(bookArgumentCaptor.getValue().getTitle()).isEqualTo("Don Quijote");

        assertThat(result).isNotNull();
        assertThat(result.getIdBook()).isEqualTo(1);
        assertThat(result.getAuthor()).isEqualTo("Don Francisco Pizarro");
        assertThat(result.getCategory()).isEqualTo("Cultura General");
        assertThat(result.getTitle()).isEqualTo("Don Quijote");
        assertThat(result.isAvailable()).isEqualTo(true);
    }

    @Test
    void testUpdateBookWhenTheBookNotExists() {
        // Given
        int id = 99999;

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> bookService.updateBook(new UpdateBookDTO("Title", "Name Last Name", "Category"), id));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El libro con ID 99999 no existe");
    }

    @Test
    void testDeleteBook() {
        // Given
        int id = 5;
        Book book = books.getLast();
        List<Book> booksAfterDelete = List.of(
                new Book(1, true, "Cien Años de Soledad", "Gabriel García Márquez", "Novela", null),
                new Book(2, true, "La Casa de los Espíritus", "Isabel Allende", "Novela", null),
                new Book(3, false, "Harry Potter y la Piedra Filosofal", "J.K. Rowling", "Fantasía", null),
                new Book(4, true, "1984", "George Orwell", "Distopía", null));
        Page<Book> page = new PageImpl<>(booksAfterDelete.subList(0,4), PageRequest.of(0, 4), booksAfterDelete.size());

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        List<BookDTO> result = bookService.deleteBook(id);

        // Then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).delete(bookArgumentCaptor.capture());
        assertThat(bookArgumentCaptor.getValue().getIdBook()).isEqualTo(id);

        assertThat(result).hasSize(4);
        assertThat(result.getFirst().getIdBook()).isEqualTo(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Cien Años de Soledad");
        assertThat(result.get(2).getAuthor()).isEqualTo("J.K. Rowling");
        assertThat(result.getLast().getCategory()).isEqualTo("Distopía");
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

    @Test
    void testGetBook() {
        // Given
        int id = 1;
        Book book = books.getFirst();

        // When
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Book result = bookService.getBook(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdBook()).isEqualTo(1);
        assertThat(result.getCategory()).isEqualTo("Novela");
        assertThat(result.getAuthor()).isEqualTo("Gabriel García Márquez");
        assertThat(result.getTitle()).isEqualTo("Cien Años de Soledad");
    }

    @Test
    void testGetBookWhenBookNotExists() {
        // Give
        int id = 99999;

        // When
        ResourceNotExistsException ex = assertThrows(ResourceNotExistsException.class, () -> bookService.deleteBook(id));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El libro con ID 99999 no existe");
    }

    @Test
    void testReturnBookDTO() {
        // Give
        Book book = books.getFirst();

        // When

        BookDTO result = bookService.returnBookDTO(book);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdBook()).isEqualTo(1);
        assertThat(result.isAvailable()).isEqualTo(true);
        assertThat(result.getAuthor()).isEqualTo("Gabriel García Márquez");
        assertThat(result.getCategory()).isEqualTo("Novela");
        assertThat(result.getTitle()).isEqualTo("Cien Años de Soledad");
    }
}
