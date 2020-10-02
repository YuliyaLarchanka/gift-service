package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.EmptyUpdateParameterException;
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
        List<Tag> tags = certificate.getTagList();
        tags = fetchExistedTagsOfCertificateToCreate(tags);
        if (!tags.isEmpty()) {
            tags = tags.stream()
                    .map(this::createTagIfNotExist)
                    .collect(Collectors.toList());
        }
        certificate.setTagList(tags);
        return repository.create(certificate);
    }

    private List<Tag> fetchExistedTagsOfCertificateToCreate(List<Tag> tags){
        return tags.stream().filter(tag -> tag.getName().length()>2)
                .peek(tag -> tag.setName(tag.getName().toLowerCase()))
                .collect(Collectors.toList());
    }

    private Tag createTagIfNotExist(Tag tag){
        Tag tagToCreate = fillTag(tag);
        Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
        if (tagOptional.isEmpty()){
            return tagRepository.create(tagToCreate);
        }else if (tagOptional.isPresent() && tagOptional.get().isDeleted()){
            Tag tag1 = tagOptional.get();
            tag1.setDeleted(false);
            return tag1;
        }
        return tagOptional.get();
    }

    private Tag fillTag(Tag tag){
        Tag tagToCreate = new Tag();
        String name = tag.getName();
        tagToCreate.setName(name);
        tagToCreate.setDeleted(false);
        return tagToCreate;
    }

    @Override
    @Transactional
    public Optional<Certificate> update(Certificate certificate) {
        List<Tag> tags = convertTagsToLowerCase(certificate);
        certificate.setTagList(tags);
        Optional<Certificate> existedCertificateOptional = repository.findById(certificate.getId(), Certificate.class);
        if (existedCertificateOptional.isEmpty() || existedCertificateOptional.get().isDeleted()) {
            return Optional.empty();
        }
        Certificate existedCertificate = existedCertificateOptional.get();
        fillCertificate(existedCertificate, certificate);
        return repository.update(existedCertificate);
    }

    private List<Tag> convertTagsToLowerCase(Certificate certificate){
       return certificate.getTagList().stream().map(tag -> {
            tag.setName(tag.getName().toLowerCase());
            return tag;
        }).collect(Collectors.toList());
    }

    private Certificate fillCertificate(Certificate existedCertificate, Certificate certificate){
        existedCertificate.setName(certificate.getName());
        existedCertificate.setDescription(certificate.getDescription());
        existedCertificate.setPrice(certificate.getPrice());
        existedCertificate.setDurationInDays(certificate.getDurationInDays());
        List<Tag> tagList = certificate.getTagList();
        tagList = tagList.stream().map(this::createTagIfNotExist).collect(Collectors.toList());
        existedCertificate.setTagList(tagList);
        return existedCertificate;
    }

    @Override
    public Optional<Certificate> updateOneField(Certificate certificate) {
        Optional<Certificate> certificateToUpdateOptional = repository.findById(certificate.getId(), Certificate.class);
        if (certificateToUpdateOptional.isEmpty() || certificateToUpdateOptional.get().isDeleted()) {
            return Optional.empty();
        }
        Certificate certificateToUpdate = certificateToUpdateOptional.get();
        findFieldToUpdate(certificate, certificateToUpdate);
        return repository.update(certificateToUpdate);
    }

    private void findFieldToUpdate(Certificate certificate, Certificate certificateToUpdate){
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
    }

    @Override
    public Page<Certificate> filterCertificatesByTagAndPrice(List<String> tagNames, String price){
        if (!StringUtils.isEmpty(price) && !"max".equalsIgnoreCase(price) && !"min".equalsIgnoreCase(price)) {
            throw new WrongPriceValueParameter("Wrong price parameter value '" + price + "' ");
        }
        price = price.equalsIgnoreCase("min")? "asc" : "desc";
        return repository.filterCertificatesByTagAndPrice(tagNames, price);
    }
}
