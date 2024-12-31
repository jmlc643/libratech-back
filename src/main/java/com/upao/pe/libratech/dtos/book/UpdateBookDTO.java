package com.upao.pe.libratech.dtos.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBookDTO {
    @NotNull(message = "El titulo no puede ser null")
    @NotEmpty(message = "El titulo no puede estar vacio")
    @NotBlank(message = "El titulo no puede ser un espacio en blanco")
    private String title;
    @NotNull(message = "El autor no puede ser null")
    @NotEmpty(message = "El autor no puede estar vacio")
    @NotBlank(message = "El autor no puede ser un espacio en blanco")
    private String author;
    @NotNull(message = "La categoria no puede ser null")
    @NotEmpty(message = "La categoria no puede estar vacio")
    @NotBlank(message = "La categoria no puede ser un espacio en blanco")
    private String category;
}
