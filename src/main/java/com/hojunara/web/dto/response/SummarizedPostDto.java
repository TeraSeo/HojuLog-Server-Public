package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Image;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedPostDto {

    private String title;

    private String username;

    private double averageRate;

    private String imageUrl;

}
