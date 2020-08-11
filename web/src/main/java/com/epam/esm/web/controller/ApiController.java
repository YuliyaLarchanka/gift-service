package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Links;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.web.dto.ApiDto;
import com.epam.esm.web.dto.LinksDto;
import com.epam.esm.web.dto.PageDto;
import com.epam.esm.web.mapper.PageMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApiController<T, K extends ApiDto> {
    private final PageMapper<T, K> pageMapper;

    public ApiController(PageMapper<T, K> pageMapper){
        this.pageMapper = pageMapper;
    }

    protected Page<T> fillPageLinks(Page<T> tPage, int page, int size, String page_path){
        Links links = new Links();
        if (tPage.isHasNext()){
            links.setNext(page_path + (page + 1) + "&size=" + size);
        }
        if (tPage.isHasPrevious()){
            links.setPrevious(page_path + (page - 1) + "&size=" + size);
        }
        if (tPage.getOffset()>=0){
            links.setSelf(page_path + page + "&size=" + size);
        }
        tPage.setLinks(links);
        return tPage;
    }

    protected Page<T> fillPageLinksForFilter(Page<T> tPage, String filterPath, Map<String, String> params, List<String> tagNames){
        String descriptionPart = params.get("descriptionPart");
        String order = params.get("order");
        String price = params.get("price");

        StringBuilder path = new StringBuilder(filterPath);
        int tagNamesSize = tagNames.size()-1;

        if (tagNames.size()!=0){
            path.append("tagNames=");
            for (String name:tagNames) {
                path.append(name);
                if (tagNames.indexOf(name)!=tagNamesSize){
                    path.append(",");
                }
            }
        }
        if (descriptionPart!=null){
            path.append("&descriptionPart=").append(descriptionPart);
        }
        if (order!=null){
            path.append("&order=").append(order);
        }
        if (price!=null){
            path.append("&price=").append(price);
        }

        path.append("&page=");
        filterPath = path.toString();

        int page = Integer.parseInt(params.get("page"));
        int size = Integer.parseInt(params.get("size"));

        Links links = new Links();
        if (tPage.isHasNext()){
            links.setNext(filterPath + (page + 1) + "&size=" + size);
        }
        if (tPage.isHasPrevious()){
            links.setPrevious(filterPath + (page - 1) + "&size=" + size);
        }
        if (tPage.getOffset()>=0){
            links.setSelf(filterPath + page + "&size=" + size);
        }
        tPage.setLinks(links);
        return tPage;
    }

    protected PageDto<K> convertPageToPageDto(Page<T> page, Function<T, K> mapperFunction, String path){
        PageDto<K> pageDto = pageMapper.pageToPageDto(page);
        List<K> list = page.getContent().stream().map(mapperFunction)
                .map(varDto -> {
                    LinksDto linksDto = new LinksDto();
                    linksDto.setSelf(path + varDto.getId());
                    varDto.setLinks(linksDto);
                    return varDto;
                })
                .collect(Collectors.toList());
        pageDto.setContent(list);
        return pageDto;
    }
}
