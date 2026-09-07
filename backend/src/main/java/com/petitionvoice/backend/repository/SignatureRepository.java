package com.petitionvoice.backend.repository;

import com.petitionvoice.backend.model.AddedPetition;
import com.petitionvoice.backend.model.SignedPetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SignatureRepository extends JpaRepository<SignedPetition, Integer> {
    boolean existsByAddedPetitionIdAndEmail(Integer petitionId, String email);
    List<SignedPetition> findAllByAddedPetitionId(Integer petitionId);
    List<SignedPetition> findAllByEmail(String email);

    //Cel mai mare numar de semnaturi -> ultima semnata -> cea mai recent adaugata
    @Query("""
        SELECT sp.addedPetition
        FROM SignedPetition sp
        WHERE sp.date_signed >= :since
        GROUP BY sp.addedPetition
        ORDER BY COUNT(sp.id) DESC, MAX(sp.date_signed) DESC, sp.addedPetition.creation_date DESC""")
    List<AddedPetition> findPetitionOfTheDay(@Param("since") Date since);
}