package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.GiftCertificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, ModelMapper modelMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);
        giftCertificate.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));
        giftCertificate = giftCertificateRepository.create(giftCertificate);
        return convertGiftCertificateToDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll();
        return giftCertificateList
                .stream().map(this::convertGiftCertificateToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificateDto> findById(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        return giftCertificateOptional.map(this::convertGiftCertificateToDto);
    }


    @Override
    @Transactional
    public Optional<GiftCertificateDto> update(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificate.class);

        Optional<GiftCertificate> existedCertificate = giftCertificateRepository.findById(giftCertificate.getId());
        if (existedCertificate.isEmpty()) {
            return Optional.empty();
        }

        giftCertificate.setDateOfModification(LocalDateTime.now(ZoneOffset.UTC));
        GiftCertificate updatedCertificate = giftCertificateRepository.update(giftCertificate);
        return Optional.of(convertGiftCertificateToDto(updatedCertificate));
    }


    private GiftCertificateDto convertGiftCertificateToDto(GiftCertificate giftCertificate) {
        List<TagDto> tagDtoList = convertTagsListToDto(giftCertificate);
        GiftCertificateDto giftCertificateDto = modelMapper.map(giftCertificate, GiftCertificateDto.class);
        giftCertificateDto.setTagDtoList(tagDtoList);
        return giftCertificateDto;
    }

    private List<TagDto> convertTagsListToDto(GiftCertificate giftCertificate) {
        return giftCertificate.getTagList().stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Override
    public List<GiftCertificateDto> filterCertificates(String tagName, String textField, String order) {
        Optional<Tag> tagOptional = tagRepository.findTagByName(tagName);

        StringBuilder sql = new StringBuilder("SELECT g.gift_certificate_id, g.name, g.description, g.price," +
                " g.date_of_creation, g.date_of_modification, g.duration_in_days FROM ");

        if (textField != null) {
            sql.append("\"filter_by_text\"('").append(textField).append("') as g");
        } else {
            sql.append("gift_certificate as g");
        }

        if (tagOptional.isPresent()) {
            long id = tagOptional.get().getId();
            sql.append(" JOIN gift_certificate_m2m_tag as m2m " +
                    "ON m2m.gift_certificate_id = g.gift_certificate_id and m2m.tag_id = ").append(id);
        }

        if (order != null) {
            sql.append(" ORDER BY g.date_of_creation ").append(order.toUpperCase());
        }

        List<GiftCertificate> giftCertificateList = giftCertificateRepository.filterCertificates(sql);
        return giftCertificateList
                .stream().map(this::convertGiftCertificateToDto).collect(Collectors.toList());
    }

}
