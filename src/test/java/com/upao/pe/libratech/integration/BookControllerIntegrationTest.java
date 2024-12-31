package com.upao.pe.libratech.integration;

import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void testListBooks() throws Exception {
        mockMvc.perform(get("/book/list/").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(not(0))))
                .andExpect(jsonPath("$[0].author").value("Thomas Cormen"))
                .andExpect(jsonPath("$[1].title").value("Clean Code"));
    }

    @Test
    void testGetBook() throws Exception {
        mockMvc.perform(get("/book/1").pathInfo("/1"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idBook").value(1))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.title").value("Introduction to Algorithms"))
                .andExpect(jsonPath("$.author", notNullValue()))
                .andExpect(jsonPath("$.author").value("Thomas Cormen"))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.category").value("Computer Science"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetBookWhenBookNotExists() throws Exception {
        mockMvc.perform(get("/book/99999").pathInfo("/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El libro con ID 99999 no existe"));
    }

    @Test
    void testAddBook() throws Exception {
        CreateBookDTO request = new CreateBookDTO("Don Quijote de la Mancha", "Don Francisco Pizarro", "Ciencia Ficcion");
        String bookJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/book/create/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idBook").value(21))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.title").value("Don Quijote de la Mancha"))
                .andExpect(jsonPath("$.author", notNullValue()))
                .andExpect(jsonPath("$.author").value("Don Francisco Pizarro"))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.category").value("Ciencia Ficcion"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testAddBookWhenAtributtesAreEmptyOrNull() throws Exception{
        CreateBookDTO request = new CreateBookDTO(null, "", " ");
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/book/create/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("title")))
                .andExpect(jsonPath("$.errors", hasKey("author")))
                .andExpect(jsonPath("$.errors.title", containsInAnyOrder("El titulo no puede ser null", "El titulo no puede ser un espacio en blanco", "El titulo no puede estar vacio")))
                .andExpect(jsonPath("$.errors.author", containsInAnyOrder("El autor no puede estar vacio", "El autor no puede ser un espacio en blanco")))
                .andExpect(jsonPath("$.errors.category", containsInAnyOrder("La categoria no puede ser un espacio en blanco")));
    }

    @Test
    void testUpdateBook() throws Exception {
        UpdateBookDTO request = new UpdateBookDTO("Introducción a la Algoritmia", "Thomas Cormen", "Ciencias de la Computacion");
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/book/update/1").pathInfo("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idBook").value(1))
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.title").value("Introducción a la Algoritmia"))
                .andExpect(jsonPath("$.author", notNullValue()))
                .andExpect(jsonPath("$.author").value("Thomas Cormen"))
                .andExpect(jsonPath("$.category", notNullValue()))
                .andExpect(jsonPath("$.category").value("Ciencias de la Computacion"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testUpdateBookWhenBookNotExists() throws Exception {
        UpdateBookDTO request = new UpdateBookDTO("title", "author", "category");
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/book/update/99999").pathInfo("/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El libro con ID 99999 no existe"));
    }

    @Test
    void testUpdateBookWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateBookDTO request = new CreateBookDTO(null, "", " ");
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/book/update/1").pathInfo("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("title")))
                .andExpect(jsonPath("$.errors", hasKey("author")))
                .andExpect(jsonPath("$.errors.title", containsInAnyOrder("El titulo no puede ser null", "El titulo no puede ser un espacio en blanco", "El titulo no puede estar vacio")))
                .andExpect(jsonPath("$.errors.author", containsInAnyOrder("El autor no puede estar vacio", "El autor no puede ser un espacio en blanco")))
                .andExpect(jsonPath("$.errors.category", containsInAnyOrder("La categoria no puede ser un espacio en blanco")));
    }

    @Test
    void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/book/delete/7").pathInfo("/7"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$[6].idBook").value(8))
                .andExpect(jsonPath("$[6].title").value("Refactoring"))
                .andExpect(jsonPath("$[6].author").value("Martin Fowler"))
                .andExpect(jsonPath("$[6].category").value("Refactoring"))
                .andExpect(jsonPath("$[6].available").value(true));
    }

    @Test
    void testDeleteBookWhenBookNotExists() throws Exception {
        mockMvc.perform(delete("/book/delete/99999").pathInfo("/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El libro con ID 99999 no existe"));
    }
}
