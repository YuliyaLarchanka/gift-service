package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    private final static long VALID_ID = 1L;
    private final static long INVALID_ID = 10000000L;

    @Mock
    private TagRepository tagRepositoryMock;

    private TagService tagService;
    private TagService tagServiceSpy;
    private Tag tag1;
    private Tag tagWithId;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        tagService = new TagServiceImpl(tagRepositoryMock);
        tagServiceSpy = spy(tagService);

        tag1 = new Tag();
        tag1.setName("dolls");

        Tag tag2 = new Tag();
        tag2.setName("books");

        tagWithId = tag1;
        tagWithId.setId(VALID_ID);

        Tag tagWithInvalidId = tag1;
        tagWithInvalidId.setId(INVALID_ID);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
    }

    @Test
    public void create_Tag_OK() {
        when(tagRepositoryMock.create(tag1)).thenReturn(tagWithId);
        //when
        Tag actual = tagServiceSpy.create(tag1);
        //then
        verify(tagRepositoryMock, times(1)).create(tag1);
        assertEquals(tagWithId, actual);
    }

        @Test
        public void update_Tag_OK() {
        when(tagRepositoryMock.update(tagWithId)).thenReturn(Optional.of(tagWithId));
        //when
        Optional<Tag> actual = tagService.update(tagWithId);
        //then
        verify(tagRepositoryMock, times(1)).update(tagWithId);
        assertEquals(Optional.of(tagWithId), actual);
    }
}
