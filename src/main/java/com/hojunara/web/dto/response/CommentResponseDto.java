package com.hojunara.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentResponseDto {

    List<SummarizedCommentDto> summarizedCommentDtoList;

    Long wholeCommentsLength;

}
