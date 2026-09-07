package com.petitionvoice.backend.services;

import com.petitionvoice.backend.dto.Petition.*;
import com.petitionvoice.backend.enums.PetitionState;
import com.petitionvoice.backend.mapper.PetitionMapper;
import com.petitionvoice.backend.model.AddedPetition;
import com.petitionvoice.backend.model.SignedPetition;
import com.petitionvoice.backend.model.User;
import com.petitionvoice.backend.repository.PetitionRepository;
import com.petitionvoice.backend.repository.SignatureRepository;
import com.petitionvoice.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetitionService {

    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;
    private final UserRepository userRepository;
    private final PetitionMapper petitionMapper;

    public PetitionService(
            PetitionRepository petitionRepository,
            SignatureRepository signatureRepository,
            UserRepository userRepository,
            PetitionMapper petitionMapper
    ) {
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
        this.userRepository = userRepository;
        this.petitionMapper = petitionMapper;
    }


    public Page<PetitionResponse> getAllApprovedPetitions(String keyword, List<String> categories, String orderBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(keyword != null && keyword.isBlank())
            keyword = null;

        if(categories != null && categories.isEmpty())
            categories = null;

        if(orderBy != null && orderBy.isBlank())
            orderBy = null;

        //petitions are shown only 30 more days after deadline

        //get current date
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        //convert to Date type
        Date thirtyDaysAgoDate = java.sql.Date.valueOf(thirtyDaysAgo);

        if("trending".equalsIgnoreCase(orderBy)) {
            return petitionRepository
                    .findFilteredTrending(keyword, categories, thirtyDaysAgoDate, PetitionState.APPROVED, pageable)
                    .map(petitionMapper::toPetitionResponse);
        } else if ("neargoal".equalsIgnoreCase(orderBy)) {
            return petitionRepository
                    .findFilteredNearGoal(keyword, categories, thirtyDaysAgoDate, PetitionState.APPROVED, pageable)
                    .map(petitionMapper::toPetitionResponse);
        } else if ("leastsigned".equalsIgnoreCase(orderBy)) {
            return petitionRepository
                    .findFilteredLeastSigned(keyword, categories, thirtyDaysAgoDate, PetitionState.APPROVED, pageable)
                    .map(petitionMapper::toPetitionResponse);
        } else if ("recentlyadded".equalsIgnoreCase(orderBy)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("creation_date"), Sort.Order.asc("id")));
        } else {
            //closing soon = default
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("expiration_date"), Sort.Order.asc("id")));

        }

        return petitionRepository.findFiltered(keyword, categories, thirtyDaysAgoDate, PetitionState.APPROVED, pageable)
                .map(petitionMapper::toPetitionResponse);
    }

    public Page<PetitionResponse> getAllPetitions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creation_date").descending());
        return petitionRepository.findAllWithCreators(pageable)
                .map(petitionMapper::toPetitionResponse);
    }

        public PetitionResponse getPetitionById(Integer id) {
        AddedPetition petition = petitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Petition not found with id: " + id));
        return petitionMapper.toPetitionResponse(petition);
    }

    public List<String> getSignatures(Integer petitionId) {
        return signatureRepository.findAllByAddedPetitionId(petitionId)
                .stream()
                .map(sig -> sig.getFirst_name() + " " + sig.getLast_name())
                .collect(Collectors.toList());
    }

    public String getStatistics(Integer petitionId) {
        AddedPetition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        double progress = 0;
        if (petition.getGoal() != null && petition.getGoal() > 0) {
            progress = ((double) petition.getCount() / petition.getGoal()) * 100;
        }
        return String.format("Progress: %.2f%% (%d/%d signatures)", progress, petition.getCount(), petition.getGoal());
    }

    public PetitionResponse getPetitionOfTheDay() {
        Date since = new Date(System.currentTimeMillis() - 24L * 60 * 60 * 1000);

        return signatureRepository.findPetitionOfTheDay(since)
                .stream()
                .findFirst()
                .map(petitionMapper::toPetitionResponse)
                .orElseThrow(() -> new RuntimeException("No petition of the day found."));
    }

    @Transactional
    public PetitionResponse createPetition(PetitionCreateRequest request) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("You must be logged in to create a petition.");
        }

        AddedPetition petition = new AddedPetition();
        petition.setTitle(request.getTitle());
        petition.setCategory(request.getCategory());
        petition.setDescription(request.getDescription());
        petition.setGoal(request.getGoal());
        petition.setExpiration_date(request.getExpirationDate());

        petition.setCreation_date(new Date());
        petition.setState(PetitionState.PENDING);
        petition.setCount(0);
        petition.setCreator(currentUser);

        AddedPetition savedPetition = petitionRepository.save(petition);
        return petitionMapper.toPetitionResponse(savedPetition);
    }

    @Transactional
    public void signPetition(Integer petitionId, GuestSignatureRequest guestData) {
        AddedPetition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        String email, firstName, lastName, telephone;

        User currentUser = getCurrentUser();

        if (currentUser != null) {
            email = currentUser.getUserDetails().getEmail();
            firstName = currentUser.getFirst_name();
            lastName = currentUser.getLast_name();
            telephone = currentUser.getUserDetails().getTelephone();
        } else {
            // guest user, datele se iau din formular
            email = guestData.getEmail();
            firstName = guestData.getFirstName();
            lastName = guestData.getLastName();
            telephone = guestData.getTelephone();
        }

        // verifica daca utilizatorul a mai semnat petitia
        if (signatureRepository.existsByAddedPetitionIdAndEmail(petitionId, email)) {
            throw new RuntimeException("This email has already signed this petition!");
        }

        SignedPetition signature = new SignedPetition();
        signature.setAddedPetition(petition);
        signature.setEmail(email);
        signature.setFirst_name(firstName);
        signature.setLast_name(lastName);
        signature.setTelephone(telephone);
        signature.setDate_signed(new Date());

        signatureRepository.save(signature);

        if (petition.getCount() == null) petition.setCount(0);
        petition.setCount(petition.getCount() + 1);
        petitionRepository.save(petition);
    }

    @Transactional
    public void giveFeedback(Integer petitionId, PetitionFeedbackRequest request) {
        AddedPetition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        petition.setState(request.getState());
        petition.setFeedback(request.getFeedback());

        petitionRepository.save(petition);
    }

    @Transactional
    public PetitionResponse updatePetition(Integer petitionId, PetitionUpdateRequest request) {
        AddedPetition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new RuntimeException("Petition not found"));

        if (request.getTitle() != null) petition.setTitle(request.getTitle());
        if (request.getDescription() != null) petition.setDescription(request.getDescription());
        if (request.getGoal() != null) petition.setGoal(request.getGoal());
        if (request.getExpirationDate() != null) petition.setExpiration_date(request.getExpirationDate());

        AddedPetition updatedPetition = petitionRepository.save(petition);
        return petitionMapper.toPetitionResponse(updatedPetition);
    }

    @Transactional
    public void deletePetition(Integer id) {
        if (!petitionRepository.existsById(id)) {
            throw new RuntimeException("Petition not found with id: " + id);
        }
        petitionRepository.deleteById(id);
    }

    // extrage user-ul curent pe baza la jwt
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            String principal = authentication.getName();

            try {
                // daca e id
                if (principal.matches("\\d+")) {
                    return userRepository.findById(Integer.parseInt(principal)).orElse(null);
                }
                // daca e email
                else {
                    return userRepository.findByUserDetails_Email(principal).orElse(null);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null; // e guest user
    }

    private final String UPLOAD_DIR = "uploads/petitions/";

    @Transactional
    public void uploadImage(Integer petitionId, MultipartFile file) {
        try {
            AddedPetition petition = petitionRepository.findById(petitionId)
                    .orElseThrow(() -> new RuntimeException("Petition not found"));

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // genereaza un nume unic
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // salveaza path-ul in bd
            petition.setImageUrl("/uploads/petitions/" + fileName);
            petitionRepository.save(petition);

        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }
}
