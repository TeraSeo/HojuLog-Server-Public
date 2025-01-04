package com.hojunara.web.service;

import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SocietyPost;
import com.hojunara.web.entity.TravelPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelPostService {
    List<TravelPost> getWholePosts();

    Page<TravelPost> getCreatedAtDescPostsByPage(Pageable pageable);

    List<TravelPost> getRecent5Posts();

    TravelPost getPostById(Long id);

    Post createPost(TravelPostDto travelPostDto, MultipartFile[] images);
}
