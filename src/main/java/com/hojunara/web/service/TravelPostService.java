package com.hojunara.web.service;

import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SocietyPost;
import com.hojunara.web.entity.TravelPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelPostService {
    List<TravelPost> getWholePosts();

    TravelPost getPostById(Long id);

    Post createPost(TravelPostDto travelPostDto, MultipartFile[] images);
}
