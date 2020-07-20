package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.web.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final ModelMapper modelMapper;

    public TagController(TagService tagService, ModelMapper modelMapper) {
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public TagDto createTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = modelMapper.map(tagDto, Tag.class);
        Tag createdTag = tagService.create(tag);
        return modelMapper.map(createdTag, TagDto.class);
    }

    @GetMapping
    public List<TagDto> findTags() {
        return tagService.findAll().stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto findTagById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") Long id) {
        return tagService.findById(id).map(tag -> modelMapper.map(tag, TagDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        tagService.delete(id);
    }
}