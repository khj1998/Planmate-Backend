package com.planmate.server.dto.response.comment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEditResponseDto {
    private Boolean isSuccess;

    public static CommentEditResponseDto of() {
        return CommentEditResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
