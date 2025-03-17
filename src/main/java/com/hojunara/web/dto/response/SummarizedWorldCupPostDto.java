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
public class SummarizedWorldCupPostDto {
    private Long postId;

    private String title;

    private SubCategory subCategory;

    private String imageUrl;

    private Timestamp createdAt;
}