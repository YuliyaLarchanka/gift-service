package com.epam.esm.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "certificate")
@Data
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
}
