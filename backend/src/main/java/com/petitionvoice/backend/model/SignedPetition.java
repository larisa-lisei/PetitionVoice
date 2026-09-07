package com.petitionvoice.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "signed_petitions",
       // un email nu poate semna aceeasi petitie
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_petition", "email"})
       })
public class SignedPetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //nu poti avea o semnatura care nu apartine de nicio petitie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petition", referencedColumnName = "id", nullable = false)
    private AddedPetition addedPetition;

    //email obligatoriu si length
    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Column(nullable = false, length = 50)
    private String first_name;

    @Column(nullable = false, length = 50)
    private String last_name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date_signed;
}