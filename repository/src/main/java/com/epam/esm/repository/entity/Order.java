package com.epam.esm.repository.entity;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "account_order")
@Data
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_order_id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "account_order_m2m_certificate",
            joinColumns = @JoinColumn(name = "account_order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private List<Certificate> certificates;

    private static final Logger logger = LogManager.getLogger();


    @PrePersist
    public void onPrePersist() {
        logger.debug("INSERT");
        System.out.println("INSERT");
        setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));
    }
}
