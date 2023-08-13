package com.planmate.server.dto.request.notice;

import lombok.Getter;

@Getter
public class NoticeEditRequestDto {
    private Long noticeId;
    private String title;
    private String content;
}
