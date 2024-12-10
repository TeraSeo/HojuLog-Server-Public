package com.hojunara.web.dto.request;

import com.hojunara.web.entity.Category;
import com.hojunara.web.entity.SubCategory;
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
public class TechnologyPostDto {

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

    @Pattern(regexp = "^https://play\\.google\\.com/store/apps/details\\?id=[^&\\s]+$")
    private String playStoreUrl;

    @Pattern(regexp = "^https://apps\\.apple\\.com/[^\\s]+$")
    private String appStoreUrl;

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

    @Pattern(regexp = "^(https?:\\/\\/)?(www\\.)?(youtube\\.com\\/(watch\\?v=[\\w-]{11}|shorts\\/[\\w-]{11}|embed\\/[\\w-]{11})|youtu\\.be\\/[\\w-]{11})$")
    private String youtubeUrl;
}
