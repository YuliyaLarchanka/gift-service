package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import com.epam.esm.service.exception.WrongFilterOrderException;
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

    public CertificateServiceImpl(CertificateRepository certificateRepository, TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Certificate create(Certificate certificate) {
        return certificateRepository.create(certificate);
    }

    @Override
    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    @Override
    public Optional<Certificate> findById(Long id) {
        return certificateRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Certificate> update(Certificate certificate) {
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
        return certificateRepository.update(existedCertificate);
    }

    @Override
    public Optional<Certificate> updateOneField(Certificate certificate) {
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

        return certificateRepository.update(certificateToUpdate);
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
    public List<Certificate> filterCertificates(String tagName, String descriptionPart, String order) {
        if (!StringUtils.isEmpty(order) && !"desc".equalsIgnoreCase(order) && !"asc".equalsIgnoreCase(order)) {
            throw new WrongFilterOrderException("Wrong filter order '" + order + "' ");
        }
        return certificateRepository.filterCertificates(tagName, descriptionPart, order);
    }

    //@Override
    //    public Order create(OrderDtoDto orderDto) {
    //   Order order =
    //      return orderService.create(order);
    //    }

}
