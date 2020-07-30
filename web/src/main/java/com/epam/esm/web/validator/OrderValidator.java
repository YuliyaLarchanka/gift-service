package com.epam.esm.web.validator;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.web.dto.AccountDto;
import com.epam.esm.web.dto.CertificateDto;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.validator.annotation.ValidOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderValidator implements ConstraintValidator<ValidOrder, OrderDto> {

    @Override
    public void initialize(ValidOrder constraintAnnotation) {}

    @Override
    public boolean isValid(OrderDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        AccountDto account = value.getAccount();
        if (account.getId() == null) {
            return false;
        }

        List<CertificateDto> certificateDtoList = value.getCertificates();

        List<CertificateDto> certificatesWithId = certificateDtoList.stream()
                .filter(certificateDto -> certificateDto.getId() != null).collect(Collectors.toList());

        if(certificateDtoList.size() > certificatesWithId.size()){
            return false;
        }

        return !certificateDtoList.isEmpty();
    }
}
