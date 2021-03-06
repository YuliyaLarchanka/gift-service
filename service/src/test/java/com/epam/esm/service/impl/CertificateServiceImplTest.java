package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {
    private final static long VALID_ID = 1L;
    private final static long INVALID_ID = 10000000L;

    @Mock
    private CertificateRepository certificateRepositoryMock;
    @Mock
    private TagRepository tagRepositoryMock;

    private CertificateService certificateService;
    private CertificateService certificateServiceSpy;
    private Certificate certificate1;
    private Certificate certificate;
    private Certificate certificateWithId;
    private Certificate certificateWithInvalidId;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        certificateService = new CertificateServiceImpl(certificateRepositoryMock, tagRepositoryMock);
        certificateServiceSpy = spy(certificateService);

        certificate1 = prepareCertificates().get(0);
        certificate = prepareCertificate();
        certificateWithId = certificate;
        certificateWithId.setId(VALID_ID);
        certificateWithInvalidId = certificate;
        certificateWithInvalidId.setId(INVALID_ID);

        List<Certificate> certificates = prepareCertificates();
        Page<Certificate> certificatePage = new Page<>();
        certificatePage.setContent(certificates);
    }

    private Certificate prepareCertificate(){
        List<Tag> tags = prepareTags();
        Certificate certificate = new Certificate();
        certificate.setName("Grocery shop");
        certificate.setDescription("A certificate to buy foodstuffs in grocery shop");
        certificate.setPrice(new BigDecimal(50));
        certificate.setDateOfCreation(LocalDateTime.now());
        certificate.setDurationInDays((short) 10);
        certificate.setTagList(tags);
        return certificate;
    }

    private List<Certificate> prepareCertificates(){
        List<Tag> tags1 = prepareTags();
        List<Tag> tags2 = prepareTags();

        Certificate certificate1 = new Certificate();
        certificate1.setId(1L);
        certificate1.setName("Toy story");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags1);

        Certificate certificate2 = new Certificate();
        certificate2.setId(2L);
        certificate2.setName("Oz by");
        certificate2.setDescription("A certificate to buy items in bookshop");
        certificate2.setPrice(new BigDecimal(50));
        certificate2.setDateOfCreation(LocalDateTime.now());
        certificate2.setDurationInDays((short) 30);
        certificate2.setTagList(tags2);

        List<Certificate> certificates = new ArrayList<>();
        certificates.add(certificate1);
        certificates.add(certificate2);
        return certificates;
    }

    private List<Tag> prepareTags(){
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("dolls");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("books");
        Tag tag3 = new Tag();
        tag3.setId(3L);
        tag3.setName("magazines");

        List<Tag> tags = new ArrayList<>();
        tags.add(tag2);
        tags.add(tag3);
        return tags;
    }

    @Test
    public void create_Certificate_OK() {
        when(certificateRepositoryMock.create(certificate)).thenReturn(certificateWithId);
        //when
        Certificate actual = certificateServiceSpy.create(certificate);
        //then
        verify(certificateRepositoryMock, times(1)).create(certificate);
        assertEquals(certificateWithId, actual);
    }

    @Test
    public void update_Certificate_OK() {
        when(certificateRepositoryMock.findById(certificateWithId.getId(), Certificate.class)).thenReturn(Optional.of(certificateWithId));
        when(certificateRepositoryMock.update(certificateWithId)).thenReturn(Optional.of(certificateWithId));
        //when
        Optional<Certificate> actual = certificateService.update(certificateWithId);
        //then
        verify(certificateRepositoryMock, times(1)).findById(certificateWithId.getId(), Certificate.class);
        verify(certificateRepositoryMock, times(1)).update(certificateWithId);
        assertEquals(Optional.of(certificateWithId), actual);
    }

    @Test
    public void update_Certificate_NotFound() {
        when(certificateRepositoryMock.findById(INVALID_ID, Certificate.class)).thenReturn(Optional.empty());
        //when
        Optional<Certificate> actual = certificateService.update(certificateWithInvalidId);
        //than
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID, Certificate.class);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void find_ById_OK() {
        when(certificateRepositoryMock.findById(VALID_ID,Certificate.class)).thenReturn(Optional.of(certificateWithId));
        //when
        Optional<Certificate> actual = certificateService.findById(VALID_ID,Certificate.class);
        //then
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID,Certificate.class);
        assertEquals(Optional.of(certificateWithId), actual);
    }

    @Test
    public void find_ById_NotFound() {
        when(certificateRepositoryMock.findById(INVALID_ID,Certificate.class)).thenReturn(Optional.empty());
        //when
        Optional<Certificate> actual = certificateService.findById(INVALID_ID,Certificate.class);
        //then
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID,Certificate.class);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void updateOneField_Certificate_OK() {
        when(certificateRepositoryMock.findById(certificateWithId.getId(), Certificate.class)).thenReturn(Optional.of(certificateWithId));
        when(certificateRepositoryMock.update(certificateWithId)).thenReturn(Optional.of(certificateWithId));
        //when
        Optional<Certificate> actual = certificateService.updateOneField(certificateWithId);
        //then
        verify(certificateRepositoryMock, times(1)).findById(certificateWithId.getId(), Certificate.class);
        verify(certificateRepositoryMock, times(1)).update(certificateWithId);
        assertEquals(Optional.of(certificateWithId), actual);
    }

    @Test
    public void updateOneFiled_Certificate_NotFound() {
        when(certificateRepositoryMock.findById(INVALID_ID, Certificate.class)).thenReturn(Optional.empty());
        //when
        Optional<Certificate> actual = certificateService.updateOneField(certificateWithInvalidId);
        //than
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID, Certificate.class);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    public void delete_CertificateId_OK() {
        when(certificateRepositoryMock.findById(VALID_ID, Certificate.class)).thenReturn(Optional.of(certificate1));
        doNothing().when(certificateRepositoryMock).delete(certificate1);
        //when
        certificateService.delete(VALID_ID, Certificate.class);
        //then
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID, Certificate.class);
        verify(certificateRepositoryMock, times(1)).delete(certificate1);
    }

    @Test
    public void delete_CertificateId_NotFoundException() {
        when(certificateRepositoryMock.findById(INVALID_ID, Certificate.class)).thenReturn(Optional.empty());
        //when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                certificateService.delete(INVALID_ID, Certificate.class));
        //then
        assertNotNull(exception);
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID, Certificate.class);
        verify(certificateRepositoryMock, never()).delete(certificate1);
    }

    @Test
    public void filterCertificate_TagPrice_OK() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("Toy story");
        tagNames.add("Oz by");
        List<Certificate> certificates = new ArrayList<>();
        certificates.add(certificate1);
        Page<Certificate> certificatePage = new Page<>();
        certificatePage.setContent(certificates);

        when(certificateRepositoryMock.filterCertificatesByTagAndPrice(tagNames, "asc")).thenReturn(certificatePage);
        //when
        Page<Certificate> actual = certificateService.filterCertificatesByTagAndPrice(tagNames, "min");
        //then
        verify(certificateRepositoryMock, times(1)).filterCertificatesByTagAndPrice(tagNames, "asc");
        assertEquals(certificatePage, actual);
    }
}
