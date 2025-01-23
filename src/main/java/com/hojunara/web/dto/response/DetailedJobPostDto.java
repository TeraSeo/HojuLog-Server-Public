package com.hojunara.web.dto.response;

import com.hojunara.web.entity.JobType;
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
public class DetailedJobPostDto {
    private Long postId;

    private Long userId;

    private String title;

    private String description;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private List<String> imageUrls;

    private JobType jobType;

    private String location;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;
}
