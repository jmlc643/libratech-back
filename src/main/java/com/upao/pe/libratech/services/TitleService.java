package com.upao.pe.libratech.services;

import com.upao.pe.libratech.dtos.title.TitleDTO;
import com.upao.pe.libratech.exceptions.ResourceAlreadyExistsException;
import com.upao.pe.libratech.exceptions.ResourceNotExistsException;
import com.upao.pe.libratech.models.Title;
import com.upao.pe.libratech.repos.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    // READ
    public List<TitleDTO> findAll(Pageable pageable) {
        return titleRepository.findAll(pageable).stream().map(this::returnTitleDTO).toList();
    }

    // CREATE
    public Title createTitle(TitleDTO titleDTO) {
        if(existsTitle(titleDTO.getTitleName())) {
            throw new ResourceAlreadyExistsException("El titulo ya existe");
        }
        Title title = new Title(null, titleDTO.getTitleName(), new ArrayList<>());
        return titleRepository.save(title);
    }

    public Title updateTitle(String titleName, Integer id) {
        Title title = getTitle(id);
        if(title.getBooks().size() > 1) {
            return createTitle(new TitleDTO(titleName));
        } else {
            title.setTitleName(titleName);
            return titleRepository.save(title);
        }
    }

    public TitleDTO returnTitleDTO(Title title) {
        return new TitleDTO(title.getTitleName());
    }

    public Title getTitle(String titleName) {
        Optional<Title> title = titleRepository.findByTitleName(titleName);
        if(title.isEmpty()) {
            throw new ResourceNotExistsException("El titulo con nobmre "+titleName+" no existe");
        }
        return title.get();
    }

    private Title getTitle(Integer id) {
        Optional<Title> title = titleRepository.findById(id);
        if(title.isEmpty()) {
            throw new ResourceNotExistsException("El titulo con ID "+id+" no existe");
        }
        return title.get();
    }

    public boolean existsTitle(String titleName) {
        return titleRepository.existsByTitleName(titleName);
    }
}
