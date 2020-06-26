package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public CertificateServiceImpl(CertificateRepository certificateRepository, TagRepository tagRepository, ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Optional<Certificate> certificateOptional = certificateRepository.findByName(certificate.getName());
        if (certificateOptional.isPresent()) {
            throw new DuplicateEntityException("Certificate with the same name already exists");
        }
        certificate.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));
        certificate = certificateRepository.create(certificate);
        return convertCertificateToDto(certificate);
    }

    @Override
    public List<CertificateDto> findAll() {
        List<Certificate> certificateList = certificateRepository.findAll();
        return certificateList
                .stream().map(this::convertCertificateToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CertificateDto> findById(Long id) {
        Optional<Certificate> certificateOptional = certificateRepository.findById(id);
        return certificateOptional.map(this::convertCertificateToDto);
    }

    @Override
    @Transactional
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Optional<Certificate> existedCertificate = certificateRepository.findById(certificate.getId());
        if (existedCertificate.isEmpty()) {
            return Optional.empty();
        }
        certificate.setDateOfModification(LocalDateTime.now(ZoneOffset.UTC));
        Certificate updatedCertificate = certificateRepository.update(certificate);
        return Optional.of(convertCertificateToDto(updatedCertificate));
    }

    public CertificateDto convertCertificateToDto(Certificate certificate) {
        List<TagDto> tagDtoList = convertTagsListToDto(certificate);
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
        certificateDto.setTagDtoList(tagDtoList);
        return certificateDto;
    }

    private List<TagDto> convertTagsListToDto(Certificate certificate) {
        return certificate.getTagList().stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Optional<Certificate> certificateOptional = certificateRepository.findById(id);
        if (certificateOptional.isEmpty()) {
            throw new EntityToDeleteNotFoundException("Certificate with this id is not found");
        }
        certificateRepository.delete(id);
    }

    @Override
    public List<CertificateDto> filterCertificates(String tagName, String textField, String order) {
        List<Certificate> certificateList = certificateRepository.filterCertificates(tagName, textField, order);
        return certificateList
                .stream().map(this::convertCertificateToDto).collect(Collectors.toList());
    }

}
