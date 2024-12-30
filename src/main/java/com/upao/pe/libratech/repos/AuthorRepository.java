package com.upao.pe.libratech.repos;

import com.upao.pe.libratech.models.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Page<Author> findAll(Pageable pageable);

    boolean existsByAuthorNameAndAuthorLastName(String authorName, String authorLastName);

    Optional<Author> findByAuthorNameAndAuthorLastName(String authorName, String authorLastName);
}
