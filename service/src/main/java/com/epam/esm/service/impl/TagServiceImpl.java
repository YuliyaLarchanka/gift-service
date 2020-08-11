package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.DuplicateEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TagServiceImpl extends ApiServiceImpl<Tag, Long, TagRepository> implements TagService  {
    public TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        Optional<Tag> tagOptional = repository.findByName(tag.getName());
        if (tagOptional.isPresent() && !tagOptional.get().isDeleted()) {
            throw new DuplicateEntityException("Tag with the same name already exists");
        }else if(tagOptional.isPresent() && tagOptional.get().isDeleted()){
            Tag tag1 = tagOptional.get();
            tag1.setDeleted(false);
            return tag1;
        }
        return repository.create(tag);
    }

    @Transactional
    @Override
    public Optional<Tag> update(Tag tag) {
        return repository.update(tag);
    }
}
