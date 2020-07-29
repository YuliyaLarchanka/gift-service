package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.dto.PageDto;
import com.epam.esm.web.dto.TagDto;
import com.epam.esm.web.mapper.PageMapper;
import com.epam.esm.web.mapper.TagMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/tags")
public class TagController extends ApiController<Tag, TagDto>{
    private final TagService tagService;
    private final TagMapper tagMapper;
    private static final String PAGE_PATH = "http://localhost:8080/tags?page=";
    private static final String PATH = "http://localhost:8080/tags/";

    public TagController(TagService tagService, TagMapper tagMapper, PageMapper<Tag, TagDto> pageMapper) {
        super(pageMapper);
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @PostMapping
    public TagDto createTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = tagMapper.tagDtoToTag(tagDto);
        Tag createdTag = tagService.create(tag);
        return tagMapper.tagToTagDto(createdTag);
    }

    @GetMapping
    public PageDto<TagDto> findTags(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                    @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero")  int size) {
        Page<Tag> tagPage = tagService.findAll(page, size);
        Page<Tag> filledPage = fillPageLinks(tagPage, page, size, PAGE_PATH);
        return convertPageToPageDto(filledPage, tagMapper::tagToTagDto, PATH);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto findTagById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") Long id) {
        return tagService.findById(id, Tag.class).map(tagMapper::tagToTagDto)
                .orElseThrow(() -> new EntityNotFoundException("Tag with such id is not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        tagService.delete(id, Tag.class);
    }
}