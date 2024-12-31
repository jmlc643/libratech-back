package com.upao.pe.libratech.unit.services;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.models.Author;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.repos.AuthorRepository;
import com.upao.pe.libratech.services.AuthorService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorService authorService;

    private List<Author> authors;

    @BeforeEach
    void setUp(){
        authors = List.of(
                new Author(1, "Thomas", "Cormen", List.of(new Book(1, true, null, null, null, null), new Book(2, false, null, null, null, null))),
                new Author(2, "Robert", "Martin", null),
                new Author(3, "Andrew", "Hunt", null),
                new Author(4, "Erich", "Gamma", null),
                new Author(5, "Stuart", "Russell", List.of(new Book(1, false, null, null, null, null))));
    }

    @Test
    void testFindAll(){
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Author> page = new PageImpl<>(authors.subList(0, 3), pageable, authors.size());

        // When
        when(authorRepository.findAll(pageable)).thenReturn(page);
        List<AuthorDTO> result = authorService.findAll(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getAuthorName()).isEqualTo("Thomas");
        assertThat(result.getFirst().getAuthorLastName()).isEqualTo("Cormen");
        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testCreateAuthor() {
        // Given
        AuthorDTO author = new AuthorDTO("German", "Garmendia");
        Author savedAuthor = new Author(6, "German", "Garmendia", new ArrayList<>());

        // When
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        Author result = this.authorService.createAuthor(author);

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(this.authorRepository).save(authorArgumentCaptor.capture());
        assertEquals("German", authorArgumentCaptor.getValue().getAuthorName());
        assertEquals("Garmendia", authorArgumentCaptor.getValue().getAuthorLastName());

        assertThat(result).isNotNull();
        assertThat(result.getIdAuthor()).isEqualTo(6);
        assertThat(result.getAuthorName()).isEqualTo("German");
        assertThat(result.getAuthorLastName()).isEqualTo("Garmendia");
        assertThat(result.getBooks()).isEmpty();
    }

    @Test
    void testCreateAuthorWhenAuthorExists() {
        // Given
        AuthorDTO authorDTO = new AuthorDTO("Thomas", "Cormen");

        // When
        when(authorRepository.existsByAuthorNameAndAuthorLastName(authorDTO.getAuthorName(), authorDTO.getAuthorLastName())).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> authorService.createAuthor(authorDTO));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El autor ya existe");
    }

    @Test
    void testUpdateAuthorWhenBooksIsGreaterThanOne() {
        // Given
        int id = 1;
        Author authorBeforeUpdate = authors.getFirst();
        Author authorAfterUpdate = new Author(6, "Tomas", "Cornelio", List.of(new Book(1, true, null, authorBeforeUpdate, null, null)));

        // When
        when(authorRepository.findById(id)).thenReturn(Optional.of(authorBeforeUpdate));
        when(authorRepository.existsByAuthorNameAndAuthorLastName("Tomas", "Cornelio")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(authorAfterUpdate);

        Author result = authorService.updateAuthor(authorAfterUpdate.getAuthorName(), authorAfterUpdate.getAuthorLastName(), id);

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        assertThat(authorArgumentCaptor.getValue().getAuthorLastName()).isEqualTo("Cornelio");

        assertThat(result).isNotNull();
        assertThat(result.getIdAuthor()).isEqualTo(6);
        assertThat(result.getAuthorName()).isEqualTo("Tomas");
        assertThat(result.getAuthorLastName()).isEqualTo("Cornelio");
    }

    @Test
    void testUpdateAuthorWhenBooksIsEqualThanOne() {
        // Given
        int id = 5;
        Author authorBeforeUpdate = authors.getLast();
        Author authorAfterUpdate = new Author(5, "Stuart", "Litle", List.of(new Book(1, false, null, null, null, null)));

        // When
        when(authorRepository.findById(id)).thenReturn(Optional.of(authorBeforeUpdate));
        when(authorRepository.save(any(Author.class))).thenReturn(authorAfterUpdate);

        Author result = authorService.updateAuthor(authorAfterUpdate.getAuthorName(), authorAfterUpdate.getAuthorLastName(), id);

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorArgumentCaptor.capture());
        assertThat(authorArgumentCaptor.getValue().getAuthorName()).isEqualTo("Stuart");
        assertThat(authorArgumentCaptor.getValue().getAuthorLastName()).isEqualTo("Litle");

        assertThat(result).isNotNull();
        assertThat(result.getIdAuthor()).isEqualTo(5);
        assertThat(result.getAuthorName()).isEqualTo("Stuart");
        assertThat(result.getAuthorLastName()).isEqualTo("Litle");
    }

    @Test
    void testReturnAuthorDTO() {
        // Given
        Author author = authors.getFirst();

        // When
        AuthorDTO result = authorService.returnAuthorDTO(author);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAuthorName()).isEqualTo(author.getAuthorName());
        assertThat(result.getAuthorLastName()).isEqualTo(author.getAuthorLastName());
    }

}
