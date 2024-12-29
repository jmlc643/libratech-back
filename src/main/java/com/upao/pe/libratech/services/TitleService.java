package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.models.Title;
import com.upao.pe.libratech.repos.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    // READ
    public List<TitleDTO> findAll(Pageable pageable) {
        return titleRepository.findAll(pageable).stream().map(this::returnTitleDTO).toList();
    }

    // CREATE
    public TitleDTO createTitle(TitleDTO titleDTO) {
        if(titleRepository.existsByTitleName(titleDTO.getTitleName())) {
            throw new ResourceAlreadyExistsException("El titulo ya existe");
        }
        Title title = new Title(null, titleDTO.getTitleName(), new ArrayList<>());
        titleRepository.save(title);
        return returnTitleDTO(title);
    }

    private TitleDTO returnTitleDTO(Title title) {
        return new TitleDTO(title.getTitleName());
    }
}
