package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.entity.Candidate;
import com.hojunara.web.entity.WorldCupPost;
import com.hojunara.web.exception.EntityNotFoundException;
import com.hojunara.web.repository.CandidateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of the {@link CandidateService} interface for managing {@link Candidate} entities.
 * <p>
 * Provides methods for creating, updating, and retrieving candidates, as well as updating their victory count.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final AwsFileService awsFileService;

    @Autowired
    public CandidateServiceImpl(CandidateRepository candidateRepository, AwsFileService awsFileService) {
        this.candidateRepository = candidateRepository;
        this.awsFileService = awsFileService;
    }

    /**
     * Retrieves a {@link Candidate} by its ID.
     *
     * @param candidateId the ID of the {@link Candidate} to retrieve
     * @return the {@link Candidate} associated with the given ID
     * @throws EntityNotFoundException if no {@link Candidate} is found with the provided ID
     */
    @Override
    public Candidate getPostById(Long candidateId) {
        return candidateRepository.findById(candidateId)
            .orElseThrow(() -> new EntityNotFoundException("Candidate Not Found Exception", candidateId));
    }

    /**
     * Creates new {@link Candidate} entities with the provided titles, image URLs, and images.
     * Associates them with the given {@link WorldCupPost}.
     *
     * @param candidateTitleList the list of titles for the new {@link Candidate}
     * @param imageUrlList the list of image URLs for the new {@link Candidate}
     * @param images the images to be uploaded and associated with the {@link Candidate}
     * @param worldCupPost the {@link WorldCupPost} to associate the {@link Candidate} with
     * @param email the email of the user creating the {@link Candidate}
     */
    @Override
    public void createCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email) {
        int imageIdx = 0;
        for (int i = 0; i < candidateTitleList.size(); i++) {
            String candidateTitle = candidateTitleList.get(i);
            String imageUrl = imageUrlList.get(i);

            Candidate candidate;
            if (!imageUrl.equals("") && imageUrl != null) {
                MultipartFile image = images[imageIdx];
                String uploadedImageUrl = awsFileService.uploadPostFile(image, email);
                candidate = Candidate.builder().title(candidateTitle).imageUrl(uploadedImageUrl).worldCupPost(worldCupPost).build();
                imageIdx++;
            }
            else {
                candidate = Candidate.builder().title(candidateTitle).worldCupPost(worldCupPost).build();
            }

            candidateRepository.save(candidate);
        }
        log.info("Successfully created candidate");
    }

    /**
     * Updates existing {@link Candidate} entities with the provided titles, image URLs, and images.
     * Associates the updated {@link Candidate} with the given {@link WorldCupPost}.
     *
     * @param candidateTitleList the list of updated titles for the {@link Candidate}
     * @param imageUrlList the list of updated image URLs for the {@link Candidate}
     * @param images the images to be uploaded and associated with the {@link Candidate}
     * @param worldCupPost the {@link WorldCupPost} to associate the {@link Candidate} with
     * @param email the email of the user updating the {@link Candidate}
     */
    @Override
    public void updateCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email) {
        List<Candidate> candidates = worldCupPost.getCandidates();

        int imageIdx = 0;
        for (int i = 0; i < candidateTitleList.size(); i++) {
            Candidate originalCandidate = candidates.get(i);
            String newCandidateTitle = candidateTitleList.get(i);
            String newImageUrl = imageUrlList.get(i);

            boolean isUpdated = false;

            if (!Objects.equals(originalCandidate.getTitle(), newCandidateTitle)) {
                originalCandidate.setTitle(newCandidateTitle);
                originalCandidate.setVictoryCount(0L);
                isUpdated = true;
            }

            if (!Objects.equals(originalCandidate.getImageUrl(), newImageUrl)) {
                if (images != null) {
                    if (imageIdx < images.length) {
                        MultipartFile image = images[imageIdx];
                        awsFileService.removeProfileFile(email, originalCandidate.getImageUrl());
                        String uploadedImageUrl = awsFileService.uploadPostFile(image, email);
                        originalCandidate.setImageUrl(uploadedImageUrl);
                        imageIdx++;
                        isUpdated = true;
                    }
                }
            }

            if (isUpdated) {
                candidateRepository.save(originalCandidate);
                candidateRepository.flush();
            }
        }
        log.info("Successfully created candidate");
    }

    /**
     * Updates the victory count of a {@link Candidate}.
     *
     * @param candidateId the ID of the {@link Candidate} whose victory count will be updated
     * @return {@code true} if the victory count was updated successfully, {@code false} otherwise
     */
    @Override
    public Boolean updateVictory(Long candidateId) {
        Candidate candidate = getPostById(candidateId);
        candidate.setVictoryCount(candidate.getVictoryCount() + 1);
        candidateRepository.save(candidate);
        log.info("Successfully updated victory count");
        return true;
    }
}
