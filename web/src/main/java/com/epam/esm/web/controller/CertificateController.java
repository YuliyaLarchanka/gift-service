package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.dto.CertificateDto;
import com.epam.esm.web.dto.CertificatePatchDto;
import com.epam.esm.web.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private final ModelMapper modelMapper;

    public CertificateController(CertificateService certificateService, ModelMapper modelMapper) {
        this.certificateService = certificateService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificateDto create(@Valid @RequestBody CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
        Certificate createdCertificate = certificateService.create(certificate);
        return modelMapper.map(createdCertificate, CertificateDto.class);
    }

    @GetMapping
    public List<CertificateDto> find(@PathVariable @NotNull int page, @PathVariable @NotNull int size) {
        return null;
        //return certificateService.findAll(page, size).stream().map(this::convertCertificateToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CertificateDto findById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        return certificateService.findById(id).map(this::convertCertificateToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PutMapping("/{id}")
    public CertificateDto update(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);

        return certificateService.update(certificate).map(this::convertCertificateToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PatchMapping("/{id}")
    public CertificateDto updateOneField(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificatePatchDto certificatePatchDto) {
        certificatePatchDto.setId(id);
        Certificate certificate = modelMapper.map(certificatePatchDto, Certificate.class);
        return certificateService.updateOneField(certificate).map(this::convertCertificateToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        certificateService.delete(id);
    }


    @GetMapping("/filter")
    public List<CertificateDto> filter(@RequestParam(required = false) String tagName,
                                                   @RequestParam(required = false) String descriptionPart,
                                                   @RequestParam(required = false) String order) {
        return certificateService.filterCertificates(tagName, descriptionPart, order)
                .stream().map(this::convertCertificateToDto).collect(Collectors.toList());
    }

    protected CertificateDto convertCertificateToDto(Certificate certificate) {
        List<TagDto> tagDtoList = convertTagsListToDto(certificate);
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
        certificateDto.setTagList(tagDtoList);
        return certificateDto;
    }

    private List<TagDto> convertTagsListToDto(Certificate certificate) {
        return certificate.getTagList().stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }
}
