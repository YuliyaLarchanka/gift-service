package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.config.RepositoryTestConfig;
import com.epam.esm.repository.entity.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {RepositoryTestConfig.class, TagRepositoryImpl.class})
@Sql("/data.sql")
public class TagRepositoryImplTest {
    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = 100L;

    @Autowired
    private TagRepository tagRepository;

    private List<Tag> tags;
    private Tag tag1;
    private Tag tag2;

    @Before
    public void setUp() {
        tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("books");
        tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("magazines");
        tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
    }

    @Test
    public void find_TagId_OK() {
        Optional<Tag> expected = Optional.of(tag1);
        Optional<Tag> actual = tagRepository.findById(VALID_ID);
        assertEquals(expected, actual);
    }

    @Test
    public void find_TagId_NotFound() {
        Optional<Tag> expected = Optional.empty();
        Optional<Tag> actual = tagRepository.findById(INVALID_ID);
        assertEquals(expected, actual);
    }

    @Test
    public void findAll_NoCriteria_OK() {
        List<Tag> expected = tags;
        List<Tag> actual = tagRepository.findAll();
        assertEquals(expected, actual);
    }

    @Test
    public void create_Tag_OK() {
        Tag expected = new Tag();
        expected.setId(3);
        expected.setName("puzzles");

        Tag actual = tagRepository.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void delete_Tag_OK() {
        List<Tag> expected = new ArrayList<>();
        expected.add(tag2);
        tagRepository.delete(VALID_ID);
        List<Tag> actual = tagRepository.findAll();
        assertEquals(expected, actual);
    }
}
