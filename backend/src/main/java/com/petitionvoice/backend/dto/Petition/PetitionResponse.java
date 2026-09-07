package com.petitionvoice.backend.dto.Petition;

import com.petitionvoice.backend.enums.PetitionState;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PetitionResponse {
    private Integer id;
    private String title;
    private String category;
    private String description;
    private PetitionState state;
    private Integer goal;
    private Integer currentSignatures;
    private Date creationDate;
    private Date expirationDate;
    private String creatorName;
    private String feedback;
    private String imageUrl;
}