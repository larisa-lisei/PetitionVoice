package com.petitionvoice.backend.services;

import com.petitionvoice.backend.dto.Activity.CodeVerificationRequest;
import com.petitionvoice.backend.dto.Activity.UserStatisticsResponse;
import com.petitionvoice.backend.dto.Petition.PetitionResponse;
import com.petitionvoice.backend.dto.User.UserResponse;
import com.petitionvoice.backend.mapper.PetitionMapper;
import com.petitionvoice.backend.mapper.UserMapper;
import com.petitionvoice.backend.model.SignedPetition;
import com.petitionvoice.backend.model.User;
import com.petitionvoice.backend.repository.PetitionRepository;
import com.petitionvoice.backend.repository.SignatureRepository;
import com.petitionvoice.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserActivityService {

    private final UserRepository userRepository;
    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;
    private final UserMapper userMapper;
    private final PetitionMapper petitionMapper;
    private final EmailService emailService;

    // pe moment ok
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public UserActivityService(
            UserRepository userRepository,
            PetitionRepository petitionRepository,
            SignatureRepository signatureRepository,
            UserMapper userMapper,
            PetitionMapper petitionMapper,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
        this.userMapper = userMapper;
        this.petitionMapper = petitionMapper;
        this.emailService = emailService;
    }

    // n-am idee ce detalii :/
    public UserResponse getUserDetails(String identifier) {
        User user;
        if (identifier.matches("\\d+")) {
            user = userRepository.findById(Integer.parseInt(identifier)).orElseThrow();
        } else {
            user = userRepository.findByUserDetails_Email(identifier).orElseThrow();
        }
        return userMapper.toUserResponse(user);
    }

    public List<PetitionResponse> getSignedPetitions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String email = user.getUserDetails().getEmail();
        List<SignedPetition> signatures = signatureRepository.findAllByEmail(email);

        return signatures.stream()
                .map(sig -> petitionMapper.toPetitionResponse(sig.getAddedPetition()))
                .collect(Collectors.toList());
    }

    public List<PetitionResponse> getSubmittedPetitions(Integer userId) {
        return petitionRepository.findAllByCreatorId(userId).stream()
                .map(petitionMapper::toPetitionResponse)
                .collect(Collectors.toList());
    }

    public UserStatisticsResponse getUserStatistics(Integer userId) {
        UserStatisticsResponse stats = new UserStatisticsResponse();

        int created = petitionRepository.findAllByCreatorId(userId).size();
        stats.setCreatedPetitionsCount(created);

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            int signed = signatureRepository.findAllByEmail(user.getUserDetails().getEmail()).size();
            stats.setSignedPetitionsCount(signed);
        } else {
            stats.setSignedPetitionsCount(0);
        }

        return stats;
    }

    public void generateVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);
        emailService.sendVerificationEmail(email, code);
    }

    @Transactional
    public boolean verifyCode(CodeVerificationRequest request) {
        String storedCode = verificationCodes.get(request.getEmail());

        if (storedCode == null || !storedCode.equals(request.getCode())) {
            throw new RuntimeException("Invalid or expired verification code.");
        }

        verificationCodes.remove(request.getEmail());

        return true;
    }
}