package com.upao.pe.libratech.dtos.book;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.dtos.title.TitleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDTO {
    private Integer idBook;
    private TitleDTO title;
    private AuthorDTO author;
    private CategoryDTO category;
    private boolean available;
}
