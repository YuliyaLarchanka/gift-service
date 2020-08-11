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
@Table(name = "certificate")
public class Certificate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "certificate_id")
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

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToMany
    @JoinTable(
            name = "certificate_m2m_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tagList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

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


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certificate that = (Certificate) o;

        if (durationInDays != that.durationInDays) return false;
        if (isDeleted != that.isDeleted) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (dateOfCreation != null ? !dateOfCreation.equals(that.dateOfCreation) : that.dateOfCreation != null)
            return false;
        if (dateOfModification != null ? !dateOfModification.equals(that.dateOfModification) : that.dateOfModification != null)
            return false;
        return tagList != null ? tagList.equals(that.tagList) : that.tagList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (dateOfCreation != null ? dateOfCreation.hashCode() : 0);
        result = 31 * result + (dateOfModification != null ? dateOfModification.hashCode() : 0);
        result = 31 * result + (int) durationInDays;
        result = 31 * result + (isDeleted ? 1 : 0);
        result = 31 * result + (tagList != null ? tagList.hashCode() : 0);
        return result;
    }
}
