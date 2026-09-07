package com.petitionvoice.backend.dto.Activity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatisticsResponse {
    private Integer createdPetitionsCount;
    private Integer signedPetitionsCount;
}