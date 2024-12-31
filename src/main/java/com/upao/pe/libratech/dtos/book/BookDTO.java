package com.upao.pe.libratech.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    private Integer idBook;
    private String title;
    private String author;
    private String category;
    private boolean available;
}
