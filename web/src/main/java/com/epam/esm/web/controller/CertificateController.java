package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.dto.CertificateDto;
import com.epam.esm.web.dto.CertificatePatchDto;
import com.epam.esm.web.dto.PageDto;
import com.epam.esm.web.mapper.CertificateMapper;
import com.epam.esm.web.mapper.PageMapper;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController extends ApiController<Certificate, CertificateDto>{
    private final CertificateService certificateService;
    private final CertificateMapper certificateMapper;
    private static final String PAGE_PATH = "http://localhost:8080/certificates?page=";
    private static final String PATH = "http://localhost:8080/certificates/";
    private static final String FILTER_PATH = "http://localhost:8080/certificates/filter?";

    public CertificateController(CertificateService certificateService, CertificateMapper certificateMapper, PageMapper<Certificate,
            CertificateDto> pageMapper) {
        super(pageMapper);
        this.certificateService = certificateService;
        this.certificateMapper = certificateMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificateDto create(@Valid @RequestBody CertificateDto certificateDto) {
        Certificate certificate = certificateMapper.certificateDtoToCertificate(certificateDto);
        Certificate createdCertificate = certificateService.create(certificate);
        return certificateMapper.certificateToCertificateDto(createdCertificate);
    }

    @GetMapping
    public PageDto<CertificateDto> findCertificates(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                                    @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero") int size) {
        Page<Certificate> certificatePage = certificateService.filteredFindAll(page, size);
        Page<Certificate> filledPage = fillPageLinks(certificatePage, page, size, PAGE_PATH);
        return convertPageToPageDto(filledPage, certificateMapper::certificateToCertificateDto, PATH);
    }

    @GetMapping("/{id}")
    public CertificateDto findById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        Optional<Certificate> certificateOptional = certificateService.findById(id, Certificate.class);

        if (certificateOptional.isEmpty() || certificateOptional.get().isDeleted()){
            throw new EntityNotFoundException("Certificate with such id is not found");
        }
        return certificateMapper.certificateToCertificateDto(certificateOptional.get());
    }

    @PutMapping("/{id}")
    public CertificateDto update(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        Certificate certificate = certificateMapper.certificateDtoToCertificate(certificateDto);
        return certificateService.update(certificate).map(certificateMapper::certificateToCertificateDto)
                .orElseThrow(() -> new EntityNotFoundException("Certificate with such id is not found"));
    }

    @PatchMapping("/{id}")
    public CertificateDto updateOneField(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificatePatchDto certificatePatchDto) {
        certificatePatchDto.setId(id);
        Certificate certificate = certificateMapper.certificateDtoToCertificate(certificatePatchDto);
        return certificateService.updateOneField(certificate).map(certificateMapper::certificateToCertificateDto)
                .orElseThrow(() -> new EntityNotFoundException("Certificate with such id is not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        Optional<Certificate> certificateOptional = certificateService.findById(id, Certificate.class);
        if(certificateOptional.isEmpty() || certificateOptional.get().isDeleted()){
            throw new EntityNotFoundException("Certificate with such id is not found");
        }
        Certificate certificate = certificateOptional.get();
        certificate.setDeleted(true);
        certificateService.update(certificate);
    }

    @GetMapping("/filter")
    public PageDto<CertificateDto> filter(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                          @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero") int size,
                                          @RequestParam(required = false) String price,
                                          @RequestParam(required = false) String descriptionPart,
                                          @RequestParam(required = false) String order,
                                          @RequestParam(required = false, value = "tagNames") List<String> tagNames) {

        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("price", price);
        params.put("descriptionPart", descriptionPart);
        params.put("order", order);

        if(price!= null && tagNames.size() != 0){
            Page<Certificate> certificatePage = certificateService.filterCertificatesByTagAndPrice(tagNames, price);
            List<Certificate> certificates = certificatePage.getContent();
            List<Certificate> filteredCertificates = certificates.stream().filter(certificate -> !certificate.isDeleted()).collect(Collectors.toList());
            certificatePage.setContent(filteredCertificates);
            Page<Certificate> filledPage = fillPageLinksForFilter(certificatePage, FILTER_PATH, params, tagNames);
            return convertPageToPageDto(filledPage, certificateMapper::certificateToCertificateDto, PATH);
        }

        Page<Certificate> certificatePage = certificateService.filterCertificatesByTagAndDescription(tagNames, descriptionPart, order, page, size);
        List<Certificate> certificates = certificatePage.getContent();
        List<Certificate> filteredCertificates = certificates.stream().filter(certificate -> !certificate.isDeleted()).collect(Collectors.toList());
        certificatePage.setContent(filteredCertificates);
        Page<Certificate> filledPage = fillPageLinksForFilter(certificatePage, FILTER_PATH, params, tagNames);
        return convertPageToPageDto(filledPage, certificateMapper::certificateToCertificateDto, PATH);
    }
}
