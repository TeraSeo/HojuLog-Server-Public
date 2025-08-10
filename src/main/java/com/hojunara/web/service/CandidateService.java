package com.hojunara.web.service;

import com.hojunara.web.entity.Candidate;
import com.hojunara.web.entity.WorldCupPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing {@link Candidate} entities.
 * <p>
 * Provides methods for retrieving, creating, updating candidates, and updating victory status.
 * </p>
 *
 * @author Taejun Seo
 */
public interface CandidateService {
    Candidate getPostById(Long candidateId);

    void createCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email);

    void updateCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email);

    Boolean updateVictory(Long candidateId);
}
