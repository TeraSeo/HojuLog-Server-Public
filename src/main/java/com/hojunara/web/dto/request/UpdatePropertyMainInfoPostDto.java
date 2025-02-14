package com.hojunara.web.dto.request;

import com.hojunara.web.entity.BathroomType;
import com.hojunara.web.entity.Period;
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
public class UpdatePropertyMainInfoPostDto {
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

    private Period period;

    @NotNull
    private Long price;

    @NotNull
    private String location;

    @NotNull
    private String availableTime;

    @NotNull
    private String roomCount;

    private BathroomType bathroomType;

    @NotNull
    private Boolean isParkable;

    private Boolean isBillIncluded;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}