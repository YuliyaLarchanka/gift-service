package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.EmptyUpdateParameterException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.WrongFilterOrderException;
import com.epam.esm.service.exception.WrongPriceValueParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl extends ApiServiceImpl<Certificate, Long, CertificateRepository> implements CertificateService {
    private final TagRepository tagRepository;

    public CertificateServiceImpl(CertificateRepository certificateRepository, TagRepository tagRepository) {
        super(certificateRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        List<Tag> tagList = certificate.getTagList();
        if (!tagList.isEmpty()) {
            tagList = tagList.stream()
                    .map(this::createTagIfNotExist)
                    .collect(Collectors.toList());
        }
        certificate.setTagList(tagList);
        return repository.create(certificate);
    }

    private Tag createTagIfNotExist(Tag tag){
        Tag tagToCreate = new Tag();
        String name = tag.getName();
        tagToCreate.setName(name);

        Optional<Tag> tagOptional = tagRepository.findByName(name);
        if (tagOptional.isEmpty()){
            return tagRepository.create(tagToCreate);
        }

        return tagOptional.get();
    }

    @Override
    @Transactional
    public Optional<Certificate> update(Certificate certificate) {
        Optional<Certificate> existedCertificateOptional = repository.findById(certificate.getId(), Certificate.class);
        if (existedCertificateOptional.isEmpty()) {
            return Optional.empty();
        }
        Certificate existedCertificate = existedCertificateOptional.get();
        existedCertificate.setName(certificate.getName());
        existedCertificate.setDescription(certificate.getDescription());
        existedCertificate.setPrice(certificate.getPrice());
        existedCertificate.setDurationInDays(certificate.getDurationInDays());

        List<Tag> tagList = certificate.getTagList();
        tagList = tagList.stream().map(this::createTagIfNotExist).collect(Collectors.toList());
        existedCertificate.setTagList(tagList);
        return repository.update(existedCertificate);
    }

    @Override
    public Optional<Certificate> updateOneField(Certificate certificate) {
        Optional<Certificate> certificateToUpdateOptional = repository.findById(certificate.getId(), Certificate.class);
        if (certificateToUpdateOptional.isEmpty()) {
            return Optional.empty();
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
            throw new EmptyUpdateParameterException("Empty or invalid field value");
        }

        return repository.update(certificateToUpdate);
    }

    @Override
    public Page<Certificate> filterCertificatesByTagAndPrice(String tagName, String price){
        if (!StringUtils.isEmpty(price) && !"max".equalsIgnoreCase(price) && !"min".equalsIgnoreCase(price)) {
            throw new WrongPriceValueParameter("Wrong price parameter value '" + price + "' ");
        }

        Optional<Tag> tagOptional = tagRepository.findByName(tagName);
        if(tagOptional.isEmpty()){
            throw new EntityNotFoundException("Tag with such name is not found");
        }

        price = price.equalsIgnoreCase("min")? "asc" : "desc";
        return repository.filterCertificatesByTagAndPrice(tagName, price);
    }


    @Override
    public Page<Certificate> filterCertificatesByTagAndDescription(String tagName, String descriptionPart, String order, int page, int size) {
        if (!StringUtils.isEmpty(order) && !"desc".equalsIgnoreCase(order) && !"asc".equalsIgnoreCase(order)) {
            throw new WrongFilterOrderException("Wrong filter order '" + order + "' ");
        }

        if (tagName!=null){
            Optional<Tag> tagOptional = tagRepository.findByName(tagName);
            if(tagOptional.isEmpty()){
                throw new EntityNotFoundException("Tag with such name is not found");
            }
        }
        return repository.filterCertificatesByTagAndDescription(tagName, descriptionPart, order, page, size);
    }

}
