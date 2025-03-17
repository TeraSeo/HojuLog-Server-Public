package com.hojunara.web.service;

import com.hojunara.web.entity.Candidate;
import com.hojunara.web.entity.WorldCupPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {
    Candidate getPostById(Long id);

    void createCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email);

    void updateCandidate(List<String> candidateTitleList, List<String> imageUrlList, MultipartFile[] images, WorldCupPost worldCupPost, String email);

    Boolean updateVictory(Long candidateId);
}
