package com.hojunara.web.service;

import com.hojunara.web.dto.request.ArticlePostDto;
import com.hojunara.web.dto.request.UpdateArticlePostDto;
import com.hojunara.web.entity.ArticlePost;
import com.hojunara.web.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticlePostService {
    ArticlePost getPostById(Long postId);

    List<ArticlePost> getWholePosts();

    Page<ArticlePost> getCreatedAtDescPostsByPage(Pageable pageable);

    Post createArticle(ArticlePostDto articlePostDto, MultipartFile[] images);

    Post updateArticle(UpdateArticlePostDto updateArticlePostDto, MultipartFile[] images);

    Page<ArticlePost> getAllPostsByPageNUser(Long userId, Pageable pageable);

    List<ArticlePost> getTop5PostsByUser(Long userId);
}
