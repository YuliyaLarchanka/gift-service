package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Links;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.web.dto.LinksDto;
import com.epam.esm.web.dto.PageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PageMapper<T, K> {
    @Mapping(target = "content", ignore = true)
    default PageDto<K> pageToPageDto(Page<T> page){
        PageDto<K> pageDto = new PageDto<>();
        LinksDto linksDto = linksToLinksDto(page.getLinks());
        pageDto.setHasPrevious(page.isHasPrevious());
        pageDto.setHasNext(page.isHasNext());
        pageDto.setTotalCount(page.getTotalCount());
        pageDto.setOffset(page.getOffset());
        pageDto.setLinks(linksDto);
        return pageDto;
    }

    LinksDto linksToLinksDto(Links links);
}
