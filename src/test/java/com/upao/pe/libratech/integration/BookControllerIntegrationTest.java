package com.upao.pe.libratech.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void testListBooks() throws Exception {
        mockMvc.perform(get("/book/list/").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$[0].author.authorName").value("Thomas"))
                .andExpect(jsonPath("$[1].title.titleName").value("Clean Code"));
    }
}
