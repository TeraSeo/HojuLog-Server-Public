package com.hojunara.web.dto.response;

import com.hojunara.web.entity.SubCategory;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedSocietyPostDto {
    private Long postId;

    private String username;

    private String title;

    private String description;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private List<String> imageUrls;

    private Timestamp createdAt;

    private double viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;
}
