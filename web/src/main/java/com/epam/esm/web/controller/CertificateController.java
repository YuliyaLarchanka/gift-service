package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
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
    public CertificateDto createGiftCertificate(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.create(certificateDto);
    }

    @GetMapping
    public List<CertificateDto> findGiftCertificates() {
        return certificateService.findAll();
    }

    @GetMapping("/{id}")
    public CertificateDto findGiftCertificateById(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        return certificateService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PutMapping
    public CertificateDto updateGiftCertificate(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.update(certificateDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteGiftCertificate(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long id) {
        certificateService.delete(id);
    }


    @GetMapping("/filter")
    public List<CertificateDto> filterCertificates(@RequestParam(required = false) String tagName,
                                                   @RequestParam(required = false) String textField,
                                                   @RequestParam(required = false) String order) {
        return certificateService.filterCertificates(tagName, textField, order);
    }
}
