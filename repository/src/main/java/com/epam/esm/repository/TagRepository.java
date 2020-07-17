package com.epam.esm.repository;

import com.epam.esm.repository.entity.Tag;

import java.util.Optional;

public interface TagRepository extends ApiRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    Tag createTagIfNotExist(Tag tag);
}
