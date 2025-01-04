package com.hojunara.web.dto.response;

import com.hojunara.web.entity.SubCategory;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedTravelPostDto {
    private Long postId;

    private String username;

    private String title;

    private SubCategory subCategory;

    private String location;

    private double rate;

    private Timestamp createdAt;

    private double viewCounts;

    private List<Map<String, String>> blogContents;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;
}
