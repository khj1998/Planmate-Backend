package com.planmate.server.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
