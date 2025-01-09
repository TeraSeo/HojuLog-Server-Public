package com.hojunara.web.dto.response;

import com.hojunara.web.entity.BathroomType;
import com.hojunara.web.entity.Period;
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
public class DetailedPropertyPostDto {
    private Long postId;

    private String username;

    private String title;

    private String description;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private List<String> imageUrls;

    private Period period;

    private Long price;

    private String location;

    private String availableTime;

    private String roomCount;

    private BathroomType bathroomType;

    private Boolean isParkable;

    private Boolean isBillIncluded;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;
}
