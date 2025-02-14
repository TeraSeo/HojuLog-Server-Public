package com.hojunara.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateTransactionPostDto {
    private UpdateTransactionMainInfoPostDto updateTransactionMainInfoPostDto;
    private UpdateTransactionMediaInfoPostDto updateTransactionMediaInfoPostDto;
}
