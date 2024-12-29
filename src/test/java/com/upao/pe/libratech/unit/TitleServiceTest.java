package com.upao.pe.libratech.unit;

import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
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

import java.util.List;

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
                new Title(1, "Introduction to Algorithms", null),
                new Title(2, "Clean Code", null),
                new Title(3, "The Pragmatic Programmer", null),
                new Title(4, "Design Patterns", null),
                new Title(5, "Artificial Intelligence: A Modern Approach", null)
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

        // When
        TitleDTO result = titleService.createTitle(title);

        // Then
        ArgumentCaptor<Title> titleArgumentCaptor = ArgumentCaptor.forClass(Title.class);
        verify(titleRepository).save(titleArgumentCaptor.capture());
        assertEquals("Data Science", titleArgumentCaptor.getValue().getTitleName());

        assertThat(result).isNotNull();
        assertEquals("Data Science", result.getTitleName());
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
}
