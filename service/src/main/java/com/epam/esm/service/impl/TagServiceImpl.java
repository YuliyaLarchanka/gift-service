package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
        if (tagOptional.isPresent()) {
            throw new DuplicateEntityException("Tag with the same name already exists");
        }
        return tagRepository.create(tag);
    }

    @Override
    public Page<Tag> findAll(int page, int size) {
        return tagRepository.findAll(page, size);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Optional<Tag> update(Tag var) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new EntityToDeleteNotFoundException("Tag with this id is not found");
        }
        tagRepository.delete(tagOptional.get());
    }
}
