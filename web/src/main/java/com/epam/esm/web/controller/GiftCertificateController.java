package com.epam.esm.web.controller;

import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto createGiftCertificate(@Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.create(giftCertificateDto);
    }

    @GetMapping
    public List<GiftCertificateDto> findGiftCertificates() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findGiftCertificateById(@PathVariable long id) {
        return giftCertificateService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @PutMapping
    public GiftCertificateDto updateGiftCertificate(@Valid @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.update(giftCertificateDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteGiftCertificate(@PathVariable long id) {
        giftCertificateService.delete(id);
    }


    @GetMapping("/filter")
    public List<GiftCertificateDto> filterCertificates(@RequestParam(required = false) String tagName,
                                                       @RequestParam(required = false) String textField,
                                                       @RequestParam(required = false) String order) {
        return giftCertificateService.filterCertificates(tagName, textField, order);
    }
}
