package com.upao.pe.libratech.repos;

import com.upao.pe.libratech.models.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, Integer> {
    Page<Title> findAll(Pageable pageable);

    boolean existsByTitleName(String titleName);
}
