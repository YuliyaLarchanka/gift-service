package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Tag;
import com.epam.esm.web.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mapping(target = "links", ignore = true)
    TagDto tagToTagDto(Tag tag);

    @Mapping(target = "certificates", ignore = true)
    Tag tagDtoToTag(TagDto tagDto);
}
