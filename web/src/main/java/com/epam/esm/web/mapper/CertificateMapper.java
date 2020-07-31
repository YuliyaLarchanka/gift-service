package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.web.dto.CertificateDto;
import com.epam.esm.web.dto.CertificatePatchDto;
import com.epam.esm.web.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CertificateMapper {
    @Mapping(target = "links", ignore = true)
    CertificateDto certificateToCertificateDto(Certificate certificate);

    Certificate certificateDtoToCertificate(CertificateDto certificateDto);

    @Mapping(target = "tagList", ignore = true)
    Certificate certificateDtoToCertificate(CertificatePatchDto certificatePatchDto);

    @Mapping(target = "links", ignore = true)
    TagDto tagToTagDto(Tag tag);

    @Mapping(target = "certificates", ignore = true)
    Tag tagDtoToTag(TagDto tagDto);
}
