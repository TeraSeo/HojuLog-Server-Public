package com.hojunara.web.entity;

import com.hojunara.web.dto.request.UpdateArticleMainInfoPostDto;
import com.hojunara.web.dto.request.UpdateArticleMediaInfoPostDto;
import com.hojunara.web.dto.request.UpdateArticlePostDto;
import com.hojunara.web.dto.response.DetailedArticlePostDto;
import com.hojunara.web.dto.response.DetailedJobPostDto;
import com.hojunara.web.dto.response.NormalArticlePostDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "article_post")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ArticlePost extends Post {
    @Column(nullable = false, length = 5001) // Large content field
    private String description;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ArticleImage> images = new ArrayList<>();

    public NormalArticlePostDto convertPostToNormalArticlePostDto() {
        return NormalArticlePostDto.builder().postId(getId()).title(getTitle()).createdAt(getUpdatedAt()).viewCounts(getViewCounts()).username(getUser().getUsername()).build();
    }

    public UpdateArticlePostDto convertPostToUpdateArticlePostDto() {
        List<String> imageUrls = getImages().stream()
                .map(ArticleImage::getUrl)
                .collect(Collectors.toList());

        UpdateArticleMainInfoPostDto updateArticleMainInfoPostDto = UpdateArticleMainInfoPostDto.builder().userId(getUser().getId()).postId(getId()).title(getTitle()).description(description).isCommentAllowed(getIsCommentAllowed()).build();
        UpdateArticleMediaInfoPostDto updateArticleMediaInfoPostDto = UpdateArticleMediaInfoPostDto.builder().existingImages(imageUrls).build();

        return UpdateArticlePostDto.builder().updateArticleMainInfoPostDto(updateArticleMainInfoPostDto).updateArticleMediaInfoPostDto(updateArticleMediaInfoPostDto).build();
    }

    public DetailedArticlePostDto convertPostToDetailedArticlePostDto(String userId) {
        List<String> imageUrls = getImages().stream()
                .map(ArticleImage::getUrl)
                .collect(Collectors.toList());

        Boolean isUserLiked = false;
        if (userId != null && userId != "") {
            Long parsedId = Long.valueOf(userId);
            isUserLiked = getLikes().stream()
                    .map(PostLike::getUser)
                    .map(User::getId)
                    .anyMatch(id -> id.equals(parsedId));
        }

        return DetailedArticlePostDto.builder().postId(getId()).title(getTitle()).description(getDescription()).userId(getUser().getId()).imageUrls(imageUrls).viewCounts(getViewCounts()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getUpdatedAt()).isCommentAllowed(getIsCommentAllowed()).build();
    }
}
