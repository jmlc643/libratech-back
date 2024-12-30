package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Author;
import com.upao.pe.libratech.repos.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired private AuthorRepository authorRepository;

    // READ
    public List<AuthorDTO> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable).stream().map(this::returnAuthorDTO).toList();
    }

    // CREATE
    public Author createAuthor(AuthorDTO authorDTO) {
        if(existsAuthor(authorDTO.getAuthorName(), authorDTO.getAuthorLastName())) {
            throw new ResourceAlreadyExistsException("El autor ya existe");
        }
        Author author = new Author(null, authorDTO.getAuthorName(), authorDTO.getAuthorLastName(), new ArrayList<>());
        return authorRepository.save(author);
    }

    public Author updateAuthor(String authorName, String authorLastName, Integer id) {
        Author author = getAuthor(id);
        if(author.getBooks().size() > 1) {
            return createAuthor(new AuthorDTO(authorName, authorLastName));
        } else {
            author.setAuthorName(authorName);
            author.setAuthorLastName(authorLastName);
            return authorRepository.save(author);
        }
    }

    public AuthorDTO returnAuthorDTO(Author author) {
        return new AuthorDTO(author.getAuthorName(), author.getAuthorLastName());
    }

    public Author getAuthor(String authorName, String authorLastName) {
        Optional<Author> author = authorRepository.findByAuthorNameAndAuthorLastName(authorName, authorLastName);
        if(author.isEmpty()) {
            throw new ResourceNotExistsException("El autor " + authorName + " "+authorLastName+" no existe");
        }
        return author.get();
    }

    private Author getAuthor(Integer id) {
        Optional<Author> author = authorRepository.findById(id);
        if(author.isEmpty()) {
            throw new ResourceNotExistsException("El autor con ID "+id+" no existe");
        }
        return author.get();
    }

    public boolean existsAuthor(String authorName, String authorLastName) {
        return authorRepository.existsByAuthorNameAndAuthorLastName(authorName, authorLastName);
    }
}
