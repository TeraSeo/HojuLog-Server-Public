package com.hojunara.web.service;

import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.StudyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyPostService {
    List<StudyPost> getWholePosts();

    Page<StudyPost> getCreatedAtDescPostsByPage(Pageable pageable);

    List<StudyPost> getRecent5Posts();

    StudyPost getPostById(Long id);

    Post createPost(StudyPostDto studyPostDto, MultipartFile[] images);
}
