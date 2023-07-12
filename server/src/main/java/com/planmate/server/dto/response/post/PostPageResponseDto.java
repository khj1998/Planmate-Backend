package com.planmate.server.dto.response.post;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageResponseDto {
    private Integer totalPages;
    List<PostResponseDto> postDtoList;

    public static PostPageResponseDto of(Integer totalPages,List<PostResponseDto> postDtoList) {
        return PostPageResponseDto.builder()
                .totalPages(totalPages)
                .postDtoList(postDtoList)
                .build();
    }
}
