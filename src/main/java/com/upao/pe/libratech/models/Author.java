package com.upao.pe.libratech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "author")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"author_name", "author_last_name"})})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_author")
    private Integer idAuthor;

    @Column(name = "author_name", length = 100, nullable = false)
    private String authorName;

    @Column(name = "author_last_name", length = 150, nullable = false)
    private String authorLastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;
}
