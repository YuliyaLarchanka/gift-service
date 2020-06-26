package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityToDeleteNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceImplTest {
    private final static long VALID_ID = 1L;
    private final static long INVALID_ID = 100L;

    @Mock
    private CertificateRepository certificateRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;
    @Mock
    private TagRepository tagRepositoryMock;

    private CertificateService certificateService;
    private CertificateService certificateServiceSpy;
    private ModelMapper modelMapper;
    private Certificate certificate1;
    private CertificateDto certificateDto1;
    private Certificate certificate2;
    private CertificateDto certificateDto2;
    private Certificate certificate;
    private CertificateDto certificateDto;
    private Certificate certificateWithId;
    private CertificateDto certificateDtoWithId;
    private List<Certificate> certificates;
    private List<CertificateDto> certificateDtos;

    @Before
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        certificateService = new CertificateServiceImpl(certificateRepositoryMock, tagRepositoryMock, modelMapperMock);
        certificateServiceSpy = spy(certificateService);
        modelMapper = new ModelMapper();

        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("dolls");
        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("books");
        Tag tag3 = new Tag();
        tag3.setId(3);
        tag3.setName("magazines");

        List<Tag> tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags1.add(tag2);
        List<TagDto> tagDtos1 = tags1.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());

        List<Tag> tags2 = new ArrayList<>();
        tags2.add(tag2);
        tags2.add(tag3);
        List<TagDto> tagDtos2 = tags2.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class)).collect(Collectors.toList());

        certificate1 = new Certificate();
        certificate1.setId(1);
        certificate1.setName("Toy story shop certificate");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags1);
        certificateDto1 = modelMapper.map(certificate1, CertificateDto.class);
        certificateDto1.setTagDtoList(tagDtos1);

        certificate2 = new Certificate();
        certificate2.setId(2);
        certificate2.setName("Oz by bookshop certificate");
        certificate2.setDescription("A certificate to buy items in bookshop");
        certificate2.setPrice(new BigDecimal(50));
        certificate2.setDateOfCreation(LocalDateTime.now());
        certificate2.setDurationInDays((short) 30);
        certificate2.setTagList(tags2);
        certificateDto2 = modelMapper.map(certificate2, CertificateDto.class);
        certificateDto2.setTagDtoList(tagDtos2);

        certificates = new ArrayList<>();
        certificates.add(certificate1);
        certificates.add(certificate2);

        certificateDtos = new ArrayList<>();
        certificateDtos.add(certificateDto1);
        certificateDtos.add(certificateDto2);

        certificate = new Certificate();
        certificate.setName("Grocery shop");
        certificate.setDescription("A certificate to buy foodstuffs in grocery shop");
        certificate.setPrice(new BigDecimal(50));
        certificate.setDateOfCreation(LocalDateTime.now());
        certificate.setDurationInDays((short) 10);
        certificateDto = modelMapper.map(certificate, CertificateDto.class);

        certificateWithId = certificate;
        certificateWithId.setId(1);
        certificateDtoWithId = modelMapper.map(certificateWithId, CertificateDto.class);
    }

    @Test
    public void create_Certificate_OK() {
        when(modelMapperMock.map(certificateDto, Certificate.class)).thenReturn(certificate);
        when(certificateRepositoryMock.findByName(certificate.getName())).thenReturn(Optional.empty());
        when(certificateRepositoryMock.create(certificate)).thenReturn(certificateWithId);
        doReturn(certificateDtoWithId).when(certificateServiceSpy).convertCertificateToDto(certificateWithId);
        //when
        CertificateDto actual = certificateServiceSpy.create(certificateDto);
        //then
        verify(modelMapperMock, times(1)).map(certificateDto, Certificate.class);
        verify(certificateRepositoryMock, times(1)).findByName(certificate.getName());
        verify(certificateRepositoryMock, times(1)).create(certificate);
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificateWithId);
        assertEquals(certificateDtoWithId, actual);
    }


    @Test(expected = DuplicateEntityException.class)
    public void create_Certificate_DuplicateException() {
        when(modelMapperMock.map(certificateDto, Certificate.class)).thenReturn(certificate);
        when(certificateRepositoryMock.findByName(certificate.getName())).thenReturn(Optional.of(certificateWithId));
        //when
        certificateService.create(certificateDto);
        //than
        verify(modelMapperMock, times(1)).map(certificateDto, Certificate.class);
        verify(certificateRepositoryMock, times(1)).findByName(certificate.getName());
        verify(certificateRepositoryMock, never()).create(certificate);
    }

    @Test
    public void find_CertificateId_OK() {
        when(certificateRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(certificate1));
        doReturn(certificateDto1).when(certificateServiceSpy).convertCertificateToDto(certificate1);

        Optional<CertificateDto> expected = Optional.of(certificateDto1);
        //when
        Optional<CertificateDto> actual = certificateServiceSpy.findById(VALID_ID);
        //then
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID);
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate1);
        assertEquals(expected, actual);
    }

    @Test
    public void find_CertificateId_NotFound() {
        when(certificateRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
        //when
        certificateServiceSpy.findById(INVALID_ID);
        //then
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID);
        verify(certificateServiceSpy, never()).convertCertificateToDto(any());
    }

    @Test
    public void findAll_NoCriteria_OK() {
        when(certificateRepositoryMock.findAll()).thenReturn(certificates);
        doReturn(certificateDto1).when(certificateServiceSpy).convertCertificateToDto(certificate1);
        doReturn(certificateDto2).when(certificateServiceSpy).convertCertificateToDto(certificate2);
        //when
        List<CertificateDto> actual = certificateServiceSpy.findAll();
        //then
        verify(certificateRepositoryMock, times(1)).findAll();
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate1);
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate2);
        assertEquals(certificateDtos, actual);
    }

    @Test
    public void findAll_NoCriteria_NotFound() {
        List<Certificate> emptyList = new ArrayList<>();
        when(certificateRepositoryMock.findAll()).thenReturn(emptyList);
        //when
        certificateService.findAll();
        //then
        verify(certificateRepositoryMock, times(1)).findAll();
        verify(certificateServiceSpy, never()).convertCertificateToDto(any());
    }

    @Test
    public void update_Certificate_OK() {
        when(modelMapperMock.map(certificateDto, Certificate.class)).thenReturn(certificate);
        when(certificateRepositoryMock.findById(certificate.getId())).thenReturn(Optional.of(certificate1));
        when(certificateRepositoryMock.update(certificate)).thenReturn(certificateWithId);
        doReturn(certificateDtoWithId).when(certificateServiceSpy).convertCertificateToDto(certificateWithId);
        //when
        Optional<CertificateDto> actual = certificateServiceSpy.update(certificateDto);
        //then
        verify(modelMapperMock, times(1)).map(certificateDto, Certificate.class);
        verify(certificateRepositoryMock, times(1)).findById(certificate.getId());
        verify(certificateRepositoryMock, times(1)).update(certificate);
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificateWithId);
        assertEquals(Optional.of(certificateDtoWithId), actual);
    }

    @Test
    public void update_Certificate_NotFound() {
        when(modelMapperMock.map(certificateDto, Certificate.class)).thenReturn(certificate);
        when(certificateRepositoryMock.findById(certificate.getId())).thenReturn(Optional.empty());
        //when
        certificateService.update(certificateDto);
        //than
        verify(modelMapperMock, times(1)).map(certificateDto, Certificate.class);
        verify(certificateRepositoryMock, times(1)).findById(certificate.getId());
        verify(certificateRepositoryMock, never()).update(certificate);
    }

    @Test
    public void delete_CertificateId_OK() {
        when(certificateRepositoryMock.findById(VALID_ID)).thenReturn(Optional.of(certificate1));
        doNothing().when(certificateRepositoryMock).delete(VALID_ID);
        //when
        certificateService.delete(VALID_ID);
        //then
        verify(certificateRepositoryMock, times(1)).findById(VALID_ID);
        verify(certificateRepositoryMock, times(1)).delete(VALID_ID);
    }

    @Test(expected = EntityToDeleteNotFoundException.class)
    public void delete_CertificateId_NotFoundException() {
        when(certificateRepositoryMock.findById(INVALID_ID)).thenReturn(Optional.empty());
        //when
        certificateService.delete(INVALID_ID);
        //then
        verify(certificateRepositoryMock, times(1)).findById(INVALID_ID);
        verify(certificateRepositoryMock, never()).delete(INVALID_ID);
    }

    @Test
    public void filter_CertificateParams_OK() {
        String tagName = "magazines";
        String textField = "certificate to buy goods";
        String order = "des";

        List<Certificate> filteredCertificates = new ArrayList<>();
        filteredCertificates.add(certificate2);
        when(certificateRepositoryMock.filterCertificates(tagName, textField, order))
                .thenReturn(filteredCertificates);
        doReturn(certificateDto2).when(certificateServiceSpy).convertCertificateToDto(certificate2);

        List<CertificateDto> expected = new ArrayList<>();
        expected.add(certificateDto2);
        //when
        List<CertificateDto> actual = certificateServiceSpy
                .filterCertificates(tagName, textField, order);
        //then
        verify(certificateRepositoryMock, times(1)).filterCertificates(tagName, textField, order);
        verify(certificateServiceSpy, times(1)).convertCertificateToDto(certificate2);
        assertEquals(expected, actual);
    }

}
