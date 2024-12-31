package com.hojunara.web.dto.response;

import com.hojunara.web.entity.BathroomType;
import com.hojunara.web.entity.Period;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.Suburb;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalPropertyPostDto {
    private Long postId;

    private String title;

    private String imageUrl;

    private String location;

    private Timestamp createdAt;

    private Suburb suburb;

    private Long price;

    private Period period;

    private Long viewCounts;

    private SubCategory subCategory;

    private String roomCount;

    private BathroomType bathroomType;

    private Boolean isParkable;

    private Boolean isBillIncluded;
}
