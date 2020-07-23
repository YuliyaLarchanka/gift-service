package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.web.dto.LinksDto;
import com.epam.esm.web.dto.PageDto;
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
import java.util.Map;
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
    public PageDto<TagDto> findTags(@RequestParam Map<String,String> allParams) {
        int page = Integer.parseInt(allParams.get("page"));
        int size = Integer.parseInt(allParams.get("size"));

        LinksDto linksDto = new LinksDto();

        Page<Tag> tagPage = tagService.findAll(page, size);
        if (tagPage.isHasNext()){//todo rename to hasNext(but read about that beforehand)
            linksDto.setNext("http://localhost:8080/tags?page=" + (page + 1) + "&size=" + size);
        }

        if (tagPage.isHasPrevious()){
            linksDto.setPrevious("http://localhost:8080/tags?page=" + (page - 1) + "&size=" + size);
        }

        if (tagPage.getOffset()>=0){
            linksDto.setSelf("http://localhost:8080/tags?page=" + page + "&size=" + size);
        }

        List<TagDto> tagDtos = tagPage.getContent().stream().map(tag -> modelMapper.map(tag,TagDto.class))
                .map(tagDto -> {
                    LinksDto tagLinksDto = new LinksDto();
                    tagLinksDto.setSelf("http://localhost:8080/tags/" + tagDto.getId());
                    tagDto.setLinks(tagLinksDto);
                    return tagDto;
                })
                .collect(Collectors.toList());

        PageDto<TagDto> tagDtoPage = new PageDto<>();
        tagDtoPage.setContent(tagDtos);
        tagDtoPage.setHasPrevious(tagPage.isHasPrevious());
        tagDtoPage.setHasNext(tagPage.isHasNext());
        tagDtoPage.setTotalCount(tagPage.getTotalCount());
        tagDtoPage.setOffset(tagPage.getOffset());
        tagDtoPage.setLinks(linksDto);
        return tagDtoPage;
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