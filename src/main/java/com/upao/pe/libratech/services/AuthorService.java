package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.author.AuthorDTO;
import com.upao.pe.libratech.models.Author;
import com.upao.pe.libratech.repos.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    @Autowired private AuthorRepository authorRepository;

    // READ
    public List<AuthorDTO> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable).stream().map(this::returnAuthorDTO).toList();
    }

    // CREATE
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author(null, authorDTO.getAuthorName(), authorDTO.getAuthorLastName(), new ArrayList<>());
        authorRepository.save(author);
        return authorDTO;
    }

    private AuthorDTO returnAuthorDTO(Author author) {
        return new AuthorDTO(author.getAuthorName(), author.getAuthorLastName());
    }
}
