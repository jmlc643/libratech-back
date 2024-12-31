package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.dtos.book.BookDTO;
import com.upao.pe.libratech.dtos.book.CreateBookDTO;
import com.upao.pe.libratech.dtos.book.UpdateBookDTO;
import com.upao.pe.libratech.dtos.category.CategoryDTO;
import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Author;
import com.upao.pe.libratech.models.Book;
import com.upao.pe.libratech.models.Category;
import com.upao.pe.libratech.models.Title;
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
    @Autowired private AuthorService authorService;
    @Autowired private TitleService titleService;
    @Autowired private CategoryService categoryService;

    // READ
    public List<BookDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream().map(this::returnBookDTO).toList();
    }

    // CREATE
    public BookDTO addBook(CreateBookDTO request) {
        Author author;
        Title title;
        Category category;
        if(authorService.existsAuthor(request.getAuthorName(), request.getAuthorLastName())) {
            author = authorService.getAuthor(request.getAuthorName(), request.getAuthorLastName());
        } else {
            author = authorService.createAuthor(new AuthorDTO(request.getAuthorName(), request.getAuthorLastName()));
        }
        if(titleService.existsTitle(request.getTitle())) {
            title = titleService.getTitle(request.getTitle());
        } else {
            title = titleService.createTitle(new TitleDTO(request.getTitle()));
        }
        if(categoryService.existsCategory(request.getCategory())) {
            category = categoryService.getCategory(request.getCategory());
        } else {
            category = categoryService.createCategory(new CategoryDTO(request.getCategory()));
        }
        Book book = new Book(null, true, title, author, category, new ArrayList<>());
        bookRepository.save(book);
        return returnBookDTO(book);
    }

    // UPDATE
    public BookDTO updateBook(UpdateBookDTO request, int idBook) {
        Book book = getBook(idBook);
        // Update Author
        book.getAuthor().getBooks().remove(book);
        Author updatedAuthor = authorService.updateAuthor(request.getAuthorName(), request.getAuthorLastName(), book.getAuthor().getIdAuthor());
        book.setAuthor(updatedAuthor);
        if (!updatedAuthor.getBooks().contains(book)) updatedAuthor.getBooks().add(book);
        // Update Title
        book.getTitle().getBooks().remove(book);
        Title updatedTitle = titleService.updateTitle(request.getTitle(), book.getTitle().getIdTitle());
        book.setTitle(updatedTitle);
        if(!updatedTitle.getBooks().contains(book)) updatedTitle.getBooks().add(book);
        // Update Category
        book.getCategory().getBooks().remove(book);
        Category updatedCategory = categoryService.updateCategory(request.getCategory(), book.getCategory().getIdCategory());
        book.setCategory(updatedCategory);
        if(!updatedCategory.getBooks().contains(book)) updatedCategory.getBooks().add(book);

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
        return new BookDTO(book.getIdBook(), titleService.returnTitleDTO(book.getTitle()), authorService.returnAuthorDTO(book.getAuthor()), categoryService.returnCategoryDTO(book.getCategory()), book.isAvailable());
    }

    public Book getBook(int id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new ResourceNotExistsException("El libro con ID "+id+" no existe");
        }
        return book.get();
    }
}
