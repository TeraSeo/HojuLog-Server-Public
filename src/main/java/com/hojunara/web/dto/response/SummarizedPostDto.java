package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Category;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedPostDto {
    private Long id;

    private String title;

    private Category category;

    private Long viewCounts;

    private Timestamp createdAt;
}
