package com.petitionvoice.backend.mapper;

import com.petitionvoice.backend.dto.Petition.PetitionResponse;
import com.petitionvoice.backend.model.AddedPetition;
import org.springframework.stereotype.Component;

@Component
public class PetitionMapper {

    public PetitionResponse toPetitionResponse(AddedPetition petition) {
        PetitionResponse response = new PetitionResponse();

        response.setId(petition.getId());
        response.setTitle(petition.getTitle());
        response.setCategory(petition.getCategory());
        response.setDescription(petition.getDescription());
        response.setState(petition.getState());
        response.setGoal(petition.getGoal());
        response.setCurrentSignatures(petition.getCount());
        response.setCreationDate(petition.getCreation_date());
        response.setExpirationDate(petition.getExpiration_date());
        response.setFeedback(petition.getFeedback());
        response.setImageUrl(petition.getImageUrl());

        if (petition.getCreator() != null) {
            String fullName = petition.getCreator().getFirst_name() + " " + petition.getCreator().getLast_name();
            response.setCreatorName(fullName);
        } else {
            response.setCreatorName("Unknown");
        }

        return response;
    }
}