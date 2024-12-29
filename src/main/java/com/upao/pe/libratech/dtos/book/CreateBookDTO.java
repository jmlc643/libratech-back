package com.upao.pe.libratech.dtos.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateBookDTO {
    @NotNull(message = "El titulo no puede ser null")
    @NotEmpty(message = "El titulo no puede estar vacio")
    @NotBlank(message = "El titulo no puede ser un espacio en blanco")
    private String title;
    @NotNull(message = "El nombre no puede ser null")
    @NotEmpty(message = "El nombre no puede estar vacio")
    @NotBlank(message = "El nombre no puede ser un espacio en blanco")
    private String authorName;
    @NotNull(message = "El apellido no puede ser null")
    @NotEmpty(message = "El apellido no puede estar vacio")
    @NotBlank(message = "El apellido no puede ser un espacio en blanco")
    private String authorLastName;
    @NotNull(message = "La categoria no puede ser null")
    @NotEmpty(message = "La categoria no puede estar vacio")
    @NotBlank(message = "La categoria no puede ser un espacio en blanco")
    private String category;
}
