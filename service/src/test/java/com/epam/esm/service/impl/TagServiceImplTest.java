package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.DuplicateEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {
    private final static long VALID_ID = 1L;

    @Mock
    private TagRepository tagRepositoryMock;

    private TagService tagServiceSpy;
    private Tag tag;
    private Tag tagWithId;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        TagService tagService = new TagServiceImpl(tagRepositoryMock);
        tagServiceSpy = spy(tagService);
        tag = prepareTag();
        tagWithId = tag;
        tag.setId(VALID_ID);
    }

    private Tag prepareTag() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("asd");
        return tag;
    }

    @Test
    public void create_Tag_OK() {
        when(tagRepositoryMock.create(tag)).thenReturn(tagWithId);
        //when
        Tag actual = tagServiceSpy.create(tag);
        //then
        verify(tagRepositoryMock, times(1)).create(tag);
        assertEquals(tagWithId, actual);
    }


    @Test
    public void create_Tag_DuplicateException() {
        //given
        when(tagRepositoryMock.create(tag)).thenThrow(new DuplicateEntityException("Tag with the same name already exists"));
        String expectedMessage = "Tag with the same name already exists";

        //when
        DuplicateEntityException actualException = assertThrows(DuplicateEntityException.class,
                () -> tagServiceSpy.create(tag));

        //then
        verify(tagRepositoryMock, times(1)).create(tag);
        assertEquals(expectedMessage, actualException.getMessage());
    }
}
