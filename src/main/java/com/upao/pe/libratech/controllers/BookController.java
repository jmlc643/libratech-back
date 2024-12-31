package com.upao.pe.libratech.controllers;

import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired private BookService bookService;

    @GetMapping("/list/")
    public List<BookDTO> listBooks(@RequestParam int page) {
        return bookService.findAll(PageRequest.of(page, 10));
    }

    @GetMapping("/{id}")
    public BookDTO getBook(@PathVariable int id) {
        Book book = bookService.getBook(id);
        return bookService.returnBookDTO(book);
    }

    @PostMapping("/create/")
    public BookDTO addBook(@Valid @RequestBody CreateBookDTO request){
        return bookService.addBook(request);
    }

    @PutMapping("/update/{id}")
    public BookDTO updateBook(@Valid @RequestBody UpdateBookDTO request, @PathVariable int id){
        return bookService.updateBook(request, id);
    }

    @DeleteMapping("/delete/{id}")
    public List<BookDTO> deleteBook(@PathVariable int id){
        return bookService.deleteBook(id);
    }
}
