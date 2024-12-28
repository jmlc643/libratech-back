package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.models.Author;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                new Author(1, "Thomas", "Cormen", null),
                new Author(2, "Robert", "Martin", null),
                new Author(3, "Andrew", "Hunt", null),
                new Author(4, "Erich", "Gamma", null),
                new Author(5, "Stuart", "Russell", null));
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
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.getFirst().getAuthorName()).isEqualTo("Thomas");
        assertThat(result.getFirst().getAuthorLastName()).isEqualTo("Cormen");
        verify(authorRepository).findAll(pageable);
    }

    @Test
    void testCreateAuthor() {
        // Given
        AuthorDTO author = new AuthorDTO("German", "Garmendia");

        // When
        AuthorDTO result = this.authorService.createAuthor(author);

        // Then
        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(this.authorRepository).save(authorArgumentCaptor.capture());
        assertEquals("German", authorArgumentCaptor.getValue().getAuthorName());
        assertEquals("Garmendia", authorArgumentCaptor.getValue().getAuthorLastName());

        assertThat(result).isNotNull();
        assertThat(result.getAuthorName()).isEqualTo("German");
        assertThat(result.getAuthorLastName()).isEqualTo("Garmendia");
    }

}
