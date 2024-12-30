package com.upao.pe.libratech.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBookDTO {
    private String title;
    private String authorName;
    private String authorLastName;
    private String category;
}
