package com.hojunara.web.dto.response;

import com.hojunara.web.entity.JobType;
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
public class NormalJobPostDto {
    private Long postId;

    private String title;

    private String location;

    private Timestamp createdAt;

    private Suburb suburb;

    private JobType jobType;

    private Long viewCounts;

    private SubCategory subCategory;
}
