package com.epam.esm.repository.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift_certificate")
public class Certificate {
    private static final Logger logger = LogManager.getLogger();

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gift_certificate_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "date_of_creation")
    public LocalDateTime dateOfCreation;

    @Column(name = "date_of_modification")
    public LocalDateTime dateOfModification;

    @Column(name = "duration_in_days")
    private short durationInDays;

    @ManyToMany
    @JoinTable(
            name = "gift_certificate_m2m_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tagList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDateTime getDateOfModification() {
        return dateOfModification;
    }

    public void setDateOfModification(LocalDateTime dateOfModification) {
        this.dateOfModification = dateOfModification;
    }

    public short getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(short durationInDays) {
        this.durationInDays = durationInDays;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @PrePersist
    public void onPrePersist() {
        logger.debug("INSERT");
        System.out.println("INSERT");
        setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));
    }

    @PreUpdate
    public void onPreUpdate() {
        logger.debug("UPDATE");
        System.out.println("UPDATE");
        setDateOfModification(LocalDateTime.now(ZoneOffset.UTC));
    }
}
