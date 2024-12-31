package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.repos.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired private BookRepository bookRepository;

    // READ
    public List<BookDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream().map(this::returnBookDTO).toList();
    }

    // CREATE
    public BookDTO addBook(CreateBookDTO request) {
        Book book = new Book(null, true, request.getTitle(), request.getAuthor(), request.getCategory(), new ArrayList<>());
        bookRepository.save(book);
        return returnBookDTO(book);
    }

    // UPDATE
    public BookDTO updateBook(UpdateBookDTO request, int idBook) {
        Book book = getBook(idBook);
        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setCategory(request.getCategory());

        bookRepository.save(book);
        return returnBookDTO(book);
    }

    // DELETE
    public List<BookDTO> deleteBook(int id) {
        Book book = getBook(id);
        bookRepository.delete(book);
        return findAll(PageRequest.of(0, 10));
    }

    public BookDTO returnBookDTO(Book book) {
        return new BookDTO(book.getIdBook(), book.getTitle(), book.getAuthor(), book.getCategory(), book.isAvailable());
    }

    public Book getBook(int id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new ResourceNotExistsException("El libro con ID "+id+" no existe");
        }
        return book.get();
    }
}
