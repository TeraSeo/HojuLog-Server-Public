package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.entity.Candidate;
import com.hojunara.web.entity.WorldCupPost;
import com.hojunara.web.exception.CandidatePostNotFoundException;
import com.hojunara.web.repository.CandidateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public Candidate getPostById(Long id) {
        try {
            Optional<Candidate> c = candidateRepository.findById(id);
            if (c.isPresent()) {
                log.info("Successfully found candidate with id: {}", id);
                return c.get();
            }
            throw new CandidatePostNotFoundException("Candidate not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find candidate with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email) {
        try {
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
        } catch (Exception e) {
            log.error("Failed to create candidate", e);
            throw e;
        }
    }

    @Override
    public void updateCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email) {
        try {
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
        } catch (Exception e) {
            log.error("Failed to create candidate", e);
            throw e;
        }
    }

    @Override
    public Boolean updateVictory(Long candidateId) {
        Candidate candidate = getPostById(candidateId);
        try {
            candidate.setVictoryCount(candidate.getVictoryCount() + 1);
            candidateRepository.save(candidate);
            log.info("Successfully updated victory count");
            return true;
        } catch (Exception e) {
            log.error("Failed to update victory", e);
            throw e;
        }
    }
}
