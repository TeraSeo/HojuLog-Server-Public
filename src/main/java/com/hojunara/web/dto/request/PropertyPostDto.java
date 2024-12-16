package com.hojunara.web.dto.request;

import com.hojunara.web.entity.Category;
import com.hojunara.web.entity.Period;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.Suburb;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @NotNull
    private Boolean isPortrait;

    private Period period;

    private Long price;

    private String address;

    private String availableTime;

    private Suburb suburb;
}
