package com.hojunara.web.service;

import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SocietyPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocietyPostService {
    List<SocietyPost> getWholePosts();

    SocietyPost getPostById(Long id);

    Post createPost(SocietyPostDto societyPostDto, MultipartFile[] images);
}
