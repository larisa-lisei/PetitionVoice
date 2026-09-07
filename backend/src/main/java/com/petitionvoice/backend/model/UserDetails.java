package com.petitionvoice.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @Column(name = "user_details_id")
    private Integer id;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, length = 20)
    private String telephone;

    @Column(nullable = false, length = 255)
    private String password_hash;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_details_id")
    private User user;
} 