package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.TagNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        Tag tag = modelMapper.map(tagDto, Tag.class);
        tag = tagRepository.create(tag);
        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDto> findById(Long id) {
        return tagRepository.findById(id).map(tag -> modelMapper.map(tag, TagDto.class));
    }

    @Override
    public Optional<TagDto> update(TagDto var) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new TagNotFoundException("Tag with this id is not found");
        }
        tagRepository.delete(id);
    }
}
