package com.planmate.server.dto.response.comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPageResponseDto {
    private Integer totalPages;
    List<CommentResponseDto> commentDtoList;

    public static CommentPageResponseDto of(Integer totalPages,List<CommentResponseDto> commentDtoList) {
        return CommentPageResponseDto.builder()
                .totalPages(totalPages)
                .commentDtoList(commentDtoList)
                .build();
    }
}
