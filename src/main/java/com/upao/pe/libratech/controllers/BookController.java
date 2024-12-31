package com.upao.pe.libratech.controllers;

import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.services.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired private BookService bookService;

    @GetMapping("/list/")
    public ResponseEntity<List<BookDTO>> listBooks(@RequestParam int page) {
        return new ResponseEntity<>(bookService.findAll(PageRequest.of(page, 10)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable int id) {
        Book book = bookService.getBook(id);
        return new ResponseEntity<>(bookService.returnBookDTO(book), HttpStatus.FOUND);
    }

    @PostMapping("/create/")
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody CreateBookDTO request){
        return new ResponseEntity<>(bookService.addBook(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody UpdateBookDTO request, @PathVariable int id){
        return new ResponseEntity<>(bookService.updateBook(request, id), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<BookDTO>> deleteBook(@PathVariable int id){
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.NO_CONTENT);
    }
}
