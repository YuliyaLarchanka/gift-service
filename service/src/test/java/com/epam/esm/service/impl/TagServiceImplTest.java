package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;

    @Mock
    private TagRepository tagRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;

    private TagService tagService;
    private ModelMapper modelMapper;
    private Tag tag1;
    private TagDto tagDto1;
    private Tag tag2;
    private TagDto tagDto2;
    private Tag tagToCreate;
    private TagDto tagToCreateDto;
    private Tag tagToCreateWithId;
    private TagDto tagToCreateDtoWithId;
    private List<Tag> tags;
    private List<TagDto> tagDtos;

    @Before
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        tagService = new TagServiceImpl(tagRepositoryMock, modelMapperMock);
        modelMapper = new ModelMapper();

        tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("lego");
        tagDto1 = modelMapper.map(tag1, TagDto.class);
        tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("books");
        tagDto2 = modelMapper.map(tag2, TagDto.class);

        tagToCreate = new Tag();
        tagToCreate.setName("dolls");
        tagToCreateDto = modelMapper.map(tagToCreate, TagDto.class);

        tagToCreateWithId = tagToCreate;
        tagToCreateWithId.setId(3);
        tagToCreateDtoWithId = modelMapper.map(tagToCreateWithId, TagDto.class);

        tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto1);
        tagDtos.add(tagDto2);
    }

    @Test
    public void find_TagId_OK() {
        Tag tag = new Tag();
        tag.setId(VALID_ID);
        tag.setName("lego");
        TagDto tagDto = modelMapper.map(tag, TagDto.class);

        when(tagRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(tag));
        when(modelMapperMock.map(tag, TagDto.class)).thenReturn(tagDto);
        //when
        Optional<TagDto> actual = tagService.findById(VALID_ID);
        //then
        verify(tagRepositoryMock, times(1)).findById(VALID_ID);
        assertEquals(Optional.of(tagDto), actual);
    }

    @Test
    public void find_TagId_NotFound() {
        Optional<Tag> empty = Optional.empty();

        when(tagRepositoryMock.findById(INVALID_ID)).thenReturn(empty);
        //when
        tagService.findById(INVALID_ID);
        //than
        verify(tagRepositoryMock, times(1)).findById(INVALID_ID);
        verify(modelMapperMock, never()).map(empty, TagDto.class);
    }

    @Test
    public void findAll_NoCriteria_OK() {
        when(tagRepositoryMock.findAll()).thenReturn(tags);
        when(modelMapperMock.map(tag1, TagDto.class)).thenReturn(tagDto1);
        when(modelMapperMock.map(tag2, TagDto.class)).thenReturn(tagDto2);
        //when
        List<TagDto> actual = tagService.findAll();
        //then
        verify(tagRepositoryMock, times(1)).findAll();
        verify(modelMapperMock, times(1)).map(tag1, TagDto.class);
        verify(modelMapperMock, times(1)).map(tag2, TagDto.class);
        assertEquals(tagDtos, actual);
    }

    @Test
    public void findAll_NoCriteria_NotFound() {
        List<Tag> emptyList = new ArrayList<>();

        when(tagRepositoryMock.findAll()).thenReturn(emptyList);
        //when
        tagService.findAll();
        //than
        verify(tagRepositoryMock, times(1)).findAll();
        verify(modelMapperMock, never()).map(emptyList, TagDto.class);
    }

    @Test
    public void create_Tag_OK() {
        when(modelMapperMock.map(tagToCreateDto, Tag.class)).thenReturn(tagToCreate);
        when(tagRepositoryMock.create(tagToCreate)).thenReturn(tagToCreateWithId);
        when(modelMapperMock.map(tagToCreateWithId, TagDto.class)).thenReturn(tagToCreateDtoWithId);
        //when
        TagDto actual = tagService.create(tagToCreateDto);
        //then
        verify(tagRepositoryMock, times(1)).create(tagToCreate);
        verify(modelMapperMock, times(1)).map(tagToCreateDto, Tag.class);
        verify(modelMapperMock, times(1)).map(tagToCreateWithId, TagDto.class);
        assertEquals(tagToCreateDtoWithId, actual);
    }

    @Test(expected = DuplicateEntityException.class)
    public void create_Tag_DuplicateException() {
        when(modelMapperMock.map(tagToCreateDto, Tag.class)).thenReturn(tagToCreate);
        when(tagRepositoryMock.findByName(tagToCreate.getName())).thenReturn(Optional.of(tagToCreateWithId));
        //when
        tagService.create(tagToCreateDto);
        //than
        verify(modelMapperMock, times(1)).map(tagToCreateDto, Tag.class);
        verify(tagRepositoryMock, times(1)).create(tagToCreate);
        verify(modelMapperMock, never()).map(tagToCreate, TagDto.class);
    }

    @Test
    public void delete_TagId_OK() {
        when(tagRepositoryMock.findById(VALID_ID)).thenReturn(Optional.ofNullable(tag1));
        doNothing().when(tagRepositoryMock).delete(VALID_ID);
        //when
        tagService.delete(VALID_ID);
        //then
        verify(tagRepositoryMock, times(1)).findById(VALID_ID);
        verify(tagRepositoryMock, times(1)).delete(VALID_ID);
    }

    @Test(expected = EntityToDeleteNotFoundException.class)
    public void delete_TagId_NotFoundException() {
        when(tagRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
        //when
        tagService.delete(INVALID_ID);
        //then
        verify(tagRepositoryMock, times(1)).findById(INVALID_ID);
        verify(tagRepositoryMock, never()).delete(INVALID_ID);
    }
}
