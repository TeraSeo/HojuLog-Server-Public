package com.hojunara.web.service;

import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SocietyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocietyPostService {
    List<SocietyPost> getWholePosts();

    Page<SocietyPost> getCreatedAtDescPostsByPage(Pageable pageable);

    List<SocietyPost> getRecent5Posts();

    SocietyPost getPostById(Long id);

    Post createPost(SocietyPostDto societyPostDto, MultipartFile[] images);
}
