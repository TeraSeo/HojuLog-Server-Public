package com.hojunara.web.dto.request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdatePropertyPostDto {
    private UpdatePropertyMainInfoPostDto updatePropertyMainInfoPostDto;
    private UpdatePropertyMediaInfoPostDto updatePropertyMediaInfoPostDto;
}