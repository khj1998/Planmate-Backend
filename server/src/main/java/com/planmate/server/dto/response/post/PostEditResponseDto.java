package com.planmate.server.dto.response.post;

import com.planmate.server.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEditResponseDto {
    private Boolean isSuccess;

    public static PostEditResponseDto of() {
        return PostEditResponseDto.builder()
                .isSuccess(true)
                .build();
    }
}
