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
public class DetailedStudyPostDto {
    private Long postId;

    private String username;

    private String title;

    private String description;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private List<String> imageUrls;

    private String school;

    private String major;

    private double rate;

    private Timestamp createdAt;

    private double viewCounts;
}
