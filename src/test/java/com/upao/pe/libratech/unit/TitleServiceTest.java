package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.models.Title;
import com.upao.pe.libratech.repos.TitleRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TitleServiceTest {

    @Mock
    private TitleRepository titleRepository;

    @InjectMocks
    private TitleService titleService;

    private List<Title> titles;

    @BeforeEach
    void setUp() {
        titles = List.of(
                new Title(1, "Introduction to Algorithms", List.of(new Book(1, true, null, null, null, null), new Book(2, false, null, null, null, null))),
                new Title(2, "Clean Code", null),
                new Title(3, "The Pragmatic Programmer", null),
                new Title(4, "Design Patterns", null),
                new Title(5, "Artificial Intelligence: A Modern Approach", List.of(new Book(1, false, null, null, null, null)))
        );
    }

    @Test
    void testFindAll() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Title> page = new PageImpl<>(titles.subList(0, 3), pageable, titles.size());

        // When
        when(titleRepository.findAll(pageable)).thenReturn(page);
        List<TitleDTO> result = titleService.findAll(pageable);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getTitleName()).isEqualTo("Introduction to Algorithms");
        assertThat(result.getLast().getTitleName()).isEqualTo("The Pragmatic Programmer");
        verify(titleRepository).findAll(pageable);
    }

    @Test
    void testCreateTitle() {
        // Given
        TitleDTO title = new TitleDTO("Data Science");
        Title titleSaved = new Title(6, "Data Science", new ArrayList<>());

        // When
        when(titleRepository.save(any(Title.class))).thenReturn(titleSaved);
        Title result = titleService.createTitle(title);

        // Then
        ArgumentCaptor<Title> titleArgumentCaptor = ArgumentCaptor.forClass(Title.class);
        verify(titleRepository).save(titleArgumentCaptor.capture());
        assertEquals("Data Science", titleArgumentCaptor.getValue().getTitleName());

        assertThat(result).isNotNull();
        assertEquals(6, result.getIdTitle());
        assertEquals("Data Science", result.getTitleName());
        assertThat(result.getBooks()).isEmpty();
    }

    @Test
    void testCreateTitleWhenTitleExists() {
        // Given
        TitleDTO title = new TitleDTO("Clean Code");

        // When
        when(titleRepository.existsByTitleName(title.getTitleName())).thenReturn(true);
        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> titleService.createTitle(title));

        // Then
        assertThat(ex.getMessage()).isEqualTo("El titulo ya existe");
    }

    @Test
    void testUpdateTitleWhenBooksIsGreaterThanOne() {
        // Given
        int id = 1;
        Title titleBeforeUpdate = titles.getFirst();
        Title titleAfterUpdate = new Title(6, "Don Quijote", List.of(new Book(1, true, titleBeforeUpdate, null, null, null)));

        // When
        when(titleRepository.findById(id)).thenReturn(Optional.of(titleBeforeUpdate));
        when(titleRepository.existsByTitleName("Don Quijote")).thenReturn(false);
        when(titleRepository.save(any(Title.class))).thenReturn(titleAfterUpdate);

        Title result = titleService.updateTitle(titleAfterUpdate.getTitleName(), id);

        // Then
        ArgumentCaptor<Title> titleArgumentCaptor = ArgumentCaptor.forClass(Title.class);
        verify(titleRepository).save(titleArgumentCaptor.capture());
        assertThat(titleArgumentCaptor.getValue().getTitleName()).isEqualTo("Don Quijote");

        assertThat(result).isNotNull();
        assertThat(result.getIdTitle()).isEqualTo(6);
        assertThat(result.getTitleName()).isEqualTo("Don Quijote");
    }

    @Test
    void testUpdateTitleWhenBooksIsEqualThanOne() {
        // Given
        int id = 5;
        Title titleBeforeUpdate = titles.getLast();
        Title titleAfterUpdate = new Title(5, "Don Quijote", List.of(new Book(1, false, null, null, null, null)));

        // When
        when(titleRepository.findById(id)).thenReturn(Optional.of(titleBeforeUpdate));
        when(titleRepository.save(any(Title.class))).thenReturn(titleAfterUpdate);

        Title result = titleService.updateTitle(titleAfterUpdate.getTitleName(), id);

        // Then
        ArgumentCaptor<Title> titleArgumentCaptor = ArgumentCaptor.forClass(Title.class);
        verify(titleRepository).save(titleArgumentCaptor.capture());
        assertThat(titleArgumentCaptor.getValue().getTitleName()).isEqualTo("Don Quijote");

        assertThat(result).isNotNull();
        assertThat(result.getIdTitle()).isEqualTo(5);
        assertThat(result.getTitleName()).isEqualTo("Don Quijote");
    }

    @Test
    void testReturnTitleDTO() {
        // Given
        Title title = titles.getFirst();

        // When
        TitleDTO result = titleService.returnTitleDTO(title);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitleName()).isEqualTo(title.getTitleName());
    }
}
