package com.hojunara.web.dto.request;

import com.hojunara.web.entity.Category;
import com.hojunara.web.entity.SubCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudyPostDto {
    @NotNull
    private Long userId;

    @NotNull
    private String title;

    @NotNull
    private Category category;

    @NotNull
    private SubCategory subCategory;

    private String school;

    private String major;

    private double rate;

    private List<Map<String, String>> blogContents;
}
