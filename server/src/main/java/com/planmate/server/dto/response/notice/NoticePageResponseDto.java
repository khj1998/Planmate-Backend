package com.planmate.server.dto.response.notice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticePageResponseDto {
    private Integer totalPages;
    private List<NoticeResponseDto> noticeDtoList;

    public static NoticePageResponseDto of(Integer totalPages, List<NoticeResponseDto> noticeDtoList) {
        return NoticePageResponseDto.builder()
                .totalPages(totalPages)
                .noticeDtoList(noticeDtoList)
                .build();
    }
}
