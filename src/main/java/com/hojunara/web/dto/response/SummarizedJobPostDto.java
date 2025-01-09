package com.hojunara.web.dto.response;

import com.hojunara.web.entity.SubCategory;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedJobPostDto {
    private Long postId;

    private String title;

    private Long viewCounts;

    private SubCategory subCategory;

    private Timestamp createdAt;
}
