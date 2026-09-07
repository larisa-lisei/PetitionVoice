package com.petitionvoice.backend.repository;

import com.petitionvoice.backend.enums.PetitionState;
import com.petitionvoice.backend.model.AddedPetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Repository
public interface PetitionRepository extends JpaRepository<AddedPetition, Integer> {
    List<AddedPetition> findAllByCreatorId(Integer creatorId);

    @Query("SELECT p FROM AddedPetition p LEFT JOIN FETCH p.creator u LEFT JOIN FETCH u.userDetails")
    Page<AddedPetition> findAllWithCreators(Pageable pageable);

    @Query("""
        SELECT p FROM AddedPetition p
        LEFT JOIN FETCH p.creator u
        LEFT JOIN FETCH u.userDetails
        WHERE (:keyword IS NULL OR
                LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:categories IS NULL OR LOWER(p.category) IN :categories)
                AND (p.expiration_date >= :thirtyDaysAgo)
                AND (p.state = :state)
    """)
    Page<AddedPetition> findFiltered(
            @Param("keyword") String keyword,
            @Param("categories") List<String> categories,
            @Param("thirtyDaysAgo") Date thirtyDaysAgo,
            @Param("state") PetitionState state,
            Pageable pageable
    );

    //trending = number of signatures raised reported to when the petition was created
    //+1 to avoid division by 0 for petitions created today
    @Query("""
        SELECT p FROM AddedPetition p
        LEFT JOIN FETCH p.creator u
        LEFT JOIN FETCH u.userDetails
        WHERE (:keyword IS NULL OR
                LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:categories IS NULL OR LOWER(p.category) IN :categories)
                AND (p.expiration_date >= :thirtyDaysAgo)
                ORDER BY (p.count / (CAST((CURRENT_DATE - p.creation_date) AS double ) + 1)) DESC,
                    p.id ASC
    """)
    Page<AddedPetition> findFilteredTrending(
            @Param("keyword") String keyword,
            @Param("categories") List<String> categories,
            @Param("thirtyDaysAgo") Date thirtyDaysAgo,
            @Param("state") PetitionState state,
            Pageable pageable
    );

    //near goal = number of signatures raised reported to number of signatures needed
    @Query("""
            SELECT p FROM AddedPetition p
            LEFT JOIN FETCH p.creator u
            LEFT JOIN FETCH u.userDetails
            WHERE (:keyword IS NULL OR
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    AND (:categories IS NULL OR LOWER(p.category) IN :categories)
                    AND (p.expiration_date >= :thirtyDaysAgo)
                    AND (p.state = :state)
                    ORDER BY (p.count * 1.0 / p.goal) DESC, p.goal ASC, p.id ASC
        """)
        Page<AddedPetition> findFilteredNearGoal(
            @Param("keyword") String keyword,
            @Param("categories") List<String> categories,
            @Param("thirtyDaysAgo") Date thirtyDaysAgo,
            @Param("state") PetitionState state,
            Pageable pageable
        );

    //least signed = number of signatures raised reported to number of signatures needed
    @Query("""
            SELECT p FROM AddedPetition p
            LEFT JOIN FETCH p.creator u
            LEFT JOIN FETCH u.userDetails
            WHERE (:keyword IS NULL OR
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
                    AND (:categories IS NULL OR LOWER(p.category) IN :categories)
                    AND (p.expiration_date >= :thirtyDaysAgo)
                    AND (p.state = :state)
                    ORDER BY (p.count * 1.0 / p.goal) ASC, p.goal DESC, p.id ASC
        """)
    Page<AddedPetition> findFilteredLeastSigned(
            @Param("keyword") String keyword,
            @Param("categories") List<String> categories,
            @Param("thirtyDaysAgo") Date thirtyDaysAgo,
            @Param("state") PetitionState state,
            Pageable pageable
    );

}