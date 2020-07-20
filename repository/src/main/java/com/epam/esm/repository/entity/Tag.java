package com.epam.esm.repository.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @ManyToMany(mappedBy = "tagList")
//    private List<Certificate> certificates = new ArrayList<>();

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

//    public List<Certificate> getCertificates() {
//        return certificates;
//    }
//
//    public void setCertificates(List<Certificate> certificates) {
//        this.certificates = certificates;
//    }
}
