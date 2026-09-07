package com.petitionvoice.backend.model;

import com.petitionvoice.backend.enums.PetitionState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "added_petitions")
public class AddedPetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_user_id", referencedColumnName = "id", nullable = false)
    private User creator;

// title nu e in ADD dar whatever, presupun ca va fi de folos
    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Date expiration_date;

    @Column(nullable = false, updatable = false)
    private Date creation_date;

    @Column(nullable = false)
    private Integer goal;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetitionState state;

    private Integer count = 0;

    private String feedback;

    private String imageUrl;
}