package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {
    private final static long VALID_ID = 1L;
//    private final static long INVALID_ID = 100L;

    @Mock
    private CertificateRepository certificateRepositoryMock;
    @Mock
    private TagRepository tagRepositoryMock;

    private CertificateService certificateService;
    private CertificateService certificateServiceSpy;
    private Certificate certificate1;
    private Certificate certificate2;
    private Certificate certificate;
    private Certificate certificateWithId;
    private List<Certificate> certificates;

    @Before
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        certificateService = new CertificateServiceImpl(certificateRepositoryMock, tagRepositoryMock);
        certificateServiceSpy = spy(certificateService);

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("dolls");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("books");
        Tag tag3 = new Tag();
        tag3.setId(3L);
        tag3.setName("magazines");

        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags1.add(tag2);

        List<Tag> tags2 = new ArrayList<>();
        tags2.add(tag2);
        tags2.add(tag3);

        certificate1 = new Certificate();
        certificate1.setId(1L);
        certificate1.setName("Toy story shop certificate");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags1);

        certificate2 = new Certificate();
        certificate2.setId(2L);
        certificate2.setName("Oz by bookshop certificate");
        certificate2.setDescription("A certificate to buy items in bookshop");
        certificate2.setPrice(new BigDecimal(50));
        certificate2.setDateOfCreation(LocalDateTime.now());
        certificate2.setDurationInDays((short) 30);
        certificate2.setTagList(tags2);

        certificates = new ArrayList<>();
        certificates.add(certificate1);
        certificates.add(certificate2);


        certificate = new Certificate();
        certificate.setName("Grocery shop");
        certificate.setDescription("A certificate to buy foodstuffs in grocery shop");
        certificate.setPrice(new BigDecimal(50));
        certificate.setDateOfCreation(LocalDateTime.now());
        certificate.setDurationInDays((short) 10);
        certificate.setTagList(tags1);

        certificateWithId = certificate;
        certificateWithId.setId(1L);
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

//
//    @Test
//    public void find_CertificateId_OK() {
//        when(certificateRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(certificate1));
//        doReturn(certificateDto1).when(certificateServiceSpy).convertCertificateToDto(certificate1);
//
//        Optional<CertificateDto> expected = Optional.of(certificateDto1);
//        //when
//        Optional<CertificateDto> actual = certificateServiceSpy.findById(VALID_ID);
//        //then
//        verify(certificateRepositoryMock, times(1)).findById(VALID_ID);
//        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate1);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void find_CertificateId_NotFound() {
//        when(certificateRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
//        //when
//        certificateServiceSpy.findById(INVALID_ID);
//        //then
//        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID);
//        verify(certificateServiceSpy, never()).convertCertificateToDto(any());
//    }
//
//    @Test
//    public void findAll_NoCriteria_OK() {
//        when(certificateRepositoryMock.findAll()).thenReturn(certificates);
//        doReturn(certificateDto1).when(certificateServiceSpy).convertCertificateToDto(certificate1);
//        doReturn(certificateDto2).when(certificateServiceSpy).convertCertificateToDto(certificate2);
//        //when
//        List<CertificateDto> actual = certificateServiceSpy.findAll();
//        //then
//        verify(certificateRepositoryMock, times(1)).findAll();
//        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate1);
//        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate2);
//        assertEquals(certificateDtos, actual);
//    }
//
    @Test
    public void findAll_NoCriteria_NotFound() {
        List<Certificate> emptyList = new ArrayList<>();
        when(certificateRepositoryMock.findAll(1,2)).thenReturn(null);
        //when
        certificateService.findAll(1,2);
        //then
        verify(certificateRepositoryMock, times(1)).findAll(1,2);
    }

    @Test
    public void update_Certificate_OK() {
        when(certificateRepositoryMock.findById(VALID_ID, Certificate.class)).thenReturn(Optional.of(certificateWithId));
        when(certificateRepositoryMock.update(certificateWithId)).thenReturn(Optional.ofNullable(certificateWithId));
        //when
        Optional<Certificate> actual = certificateServiceSpy.update(certificateWithId);
        //then
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID, Certificate.class);
        verify(certificateRepositoryMock, times(1)).update(certificateWithId);
        assertEquals(Optional.of(certificateWithId), actual);
    }

    @Test
    public void update_Certificate_NotFound() {
        when(certificateRepositoryMock.findById(VALID_ID, Certificate.class)).thenReturn(Optional.empty());
        //when
        certificateService.update(certificateWithId);
        //than
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID, Certificate.class);
        verify(certificateRepositoryMock, never()).update(certificateWithId);
    }

//    @Test
//    public void delete_CertificateId_OK() {
//        when(certificateRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(certificate1));
//        doNothing().when(certificateRepositoryMock).delete(VALID_ID);
//        //when
//        certificateService.delete(VALID_ID);
//        //then
//        verify(certificateRepositoryMock, times(1)).findById(VALID_ID);
//        verify(certificateRepositoryMock, times(1)).delete(VALID_ID);
//    }
//
//    @Test(expected = EntityToDeleteNotFoundException.class)
//    public void delete_CertificateId_NotFoundException() {
//        when(certificateRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
//        //when
//        certificateService.delete(INVALID_ID);
//        //then
//        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID);
//        verify(certificateRepositoryMock, never()).delete(INVALID_ID);
//    }
//
//    @Test
//    public void filter_CertificateParams_OK() {
//        String tagName = "magazines";
//        String textField = "certificate to buy";
//        String order = "asc";
//
//        Page<Certificate> certificatePage = new Page<>();
//        certificatePage.setContent(certificates);
//
//        when(certificateRepositoryMock.filterCertificatesByTagAndDescription(tagName, textField, order, 1, 3))
//                .thenReturn(certificatePage);
//        when(tagRepositoryMock.findByName(tagName)).thenReturn(Optional.of(tag3));
//
//        Page<Certificate> expected = new Page<>();
//        expected.setContent(certificates);
//        //when
//        Page<Certificate> actual = certificateServiceSpy
//                .filterCertificatesByTagAndDescription(tagName, textField, order, 1, 3);
//        //then
//        verify(certificateRepositoryMock, times(1)).filterCertificatesByTagAndDescription(tagName, textField,
//                order, 1, 3);
//        verify(tagRepositoryMock, times(1)).findByName()
//        assertEquals(expected, actual);
//    }

}
