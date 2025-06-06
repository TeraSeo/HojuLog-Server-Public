package com.hojunara.web.dto.response;

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
public class SummarizedPropertyPostDto {
    private Long postId;

    private String title;

    private SubCategory subCategory;

    private String imageUrl;

    private String location;

    private Timestamp createdAt;

    private Period period;

    private Long price;

    private Suburb suburb;
}
