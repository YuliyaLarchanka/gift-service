package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public TagDto createTag(@Valid @RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }

    @GetMapping
    public List<TagDto> findTags() {
        return tagService.findAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto findTagById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") Long id) {
        return tagService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        tagService.delete(id);
    }
}