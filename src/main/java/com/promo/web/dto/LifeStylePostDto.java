package com.promo.web.dto;

import com.promo.web.entity.Category;
import com.promo.web.entity.SubCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LifeStylePostDto {
    @NotNull
    private String title;

    @NotNull
    private String subTitle;

    @NotNull
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private SubCategory subCategory;

    @NotNull
    @Pattern(regexp = "^https?://[^\\s]+$")
    private String webUrl;

    @NotNull
    private String visibility;

    @NotNull
    private Boolean isOwnWork;

    @Email
    private String ownerEmail;

    private List<String> tags;

    @NotNull
    private Boolean isPortrait;
}
