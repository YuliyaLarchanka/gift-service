package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CertificatePatchDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificateDto create(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.create(certificateDto);
    }

    @GetMapping
    public List<CertificateDto> find() {
        return certificateService.findAll();
    }

    @GetMapping("/{id}")
    public CertificateDto findById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        return certificateService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PutMapping("/{id}")
    public CertificateDto update(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        return certificateService.update(certificateDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PatchMapping("/{id}")
    public CertificateDto updateOneField(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id,
                                                @Valid @RequestBody CertificatePatchDto certificatePatchDto) {
        certificatePatchDto.setId(id);
        return certificateService.updateOneField(certificatePatchDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        certificateService.delete(id);
    }


    @GetMapping("/filter")
    public List<CertificateDto> filter(@RequestParam(required = false) String tagName,
                                                   @RequestParam(required = false) String descriptionPart,
                                                   @RequestParam(required = false) String order) {
        return certificateService.filterCertificates(tagName, descriptionPart, order);
    }
}
