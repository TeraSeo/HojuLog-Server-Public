package com.hojunara.web.dto.request;

import com.hojunara.web.entity.JobType;
import com.hojunara.web.entity.Suburb;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateJobMainInfoPostDto {
    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    @NotNull
    private String title;

    private String description;

    private String contact;
    private String email;

    private Suburb suburb;

    @NotNull
    private JobType jobType;

    @NotNull
    private String location;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}