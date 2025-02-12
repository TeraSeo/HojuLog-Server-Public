package com.hojunara.web.dto.request;

import com.hojunara.web.entity.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PropertyPostDto {
    @NotNull
    private Long userId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private SubCategory subCategory;

    private String contact;

    @Email
    private String email;

    private Period period;

    private Long price;

    private String location;

    private String availableTime;

    private Suburb suburb;

    private String roomCount;

    private BathroomType bathroomType;

    private Boolean isParkable;

    private Boolean isBillIncluded;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}
