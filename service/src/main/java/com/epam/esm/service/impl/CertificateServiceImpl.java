package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CertificatePatchDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import com.epam.esm.service.exception.WrongFilterOrderException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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

        Optional<Certificate> existedCertificateOptional = certificateRepository.findById(certificate.getId());
        if (existedCertificateOptional.isEmpty()) {
            return Optional.empty();
        }
        Certificate existedCertificate = existedCertificateOptional.get();
        existedCertificate.setName(certificate.getName());
        existedCertificate.setDescription(certificate.getDescription());
        existedCertificate.setPrice(certificate.getPrice());
        existedCertificate.setDurationInDays(certificate.getDurationInDays());

        List<Tag> tagList = certificate.getTagList();
        tagList = tagList.stream().map(tagRepository::createTagIfNotExist).collect(Collectors.toList());
        existedCertificate.setTagList(tagList);
        Optional<Certificate> updatedCertificateOptional = certificateRepository.update(existedCertificate);
        return updatedCertificateOptional.map(this::convertCertificateToDto);
    }

    @Override
    public Optional<CertificateDto> updateOneField(CertificatePatchDto certificatePatchDto) {
        Certificate certificate = modelMapper.map(certificatePatchDto, Certificate.class);
        Optional<Certificate> certificateToUpdateOptional = certificateRepository.findById(certificate.getId());
        if (certificateToUpdateOptional.isEmpty()) {
            throw new RuntimeException();//TODO: create exception
        }
        Certificate certificateToUpdate = certificateToUpdateOptional.get();

        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        short durationInDays = certificate.getDurationInDays();
        if(name!=null){
            certificateToUpdate.setName(name);
        } else if(description!=null){
            certificateToUpdate.setDescription(description);
        } else if(price!=null){
            certificateToUpdate.setPrice(price);
        }else if(durationInDays!=0){
            certificateToUpdate.setDurationInDays(durationInDays);
        } else{
            throw new RuntimeException();//TODO: create exception
        }

        Optional<Certificate> updatedCertificateOptional = certificateRepository.update(certificateToUpdate);
        return updatedCertificateOptional.map(this::convertCertificateToDto);
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
    @Transactional
    public void delete(Long id) {
        Optional<Certificate> certificateOptional = certificateRepository.findById(id);
        if (certificateOptional.isEmpty()) {
            throw new EntityToDeleteNotFoundException("Certificate with this id is not found");
        }
        certificateRepository.delete(certificateOptional.get());
    }

    @Override
    public List<CertificateDto> filterCertificates(String tagName, String descriptionPart, String order) {
        if (!StringUtils.isEmpty(order) && !"desc".equalsIgnoreCase(order) && !"asc".equalsIgnoreCase(order)) {
            throw new WrongFilterOrderException("Wrong filter order '" + order + "' ");
        }
        List<Certificate> certificateList = certificateRepository.filterCertificates(tagName, descriptionPart, order);
        return certificateList
                .stream().map(this::convertCertificateToDto).collect(Collectors.toList());
    }

}
