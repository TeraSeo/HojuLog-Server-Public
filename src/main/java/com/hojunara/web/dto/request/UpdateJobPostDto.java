package com.hojunara.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateJobPostDto {
    private UpdateJobMainInfoPostDto updateJobMainInfoPostDto;
    private UpdateJobMediaInfoPostDto updateJobMediaInfoPostDto;
}
