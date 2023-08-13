package com.planmate.server.service.notice;

import com.planmate.server.dto.request.notice.NoticeEditRequestDto;
import com.planmate.server.dto.request.notice.NoticeRequestDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.notice.NoticePageResponseDto;
import com.planmate.server.dto.response.notice.NoticeResponseDto;
import com.planmate.server.dto.response.post.PostPageResponseDto;
import com.planmate.server.dto.response.post.PostResponseDto;

public interface NoticeService {
    Boolean createNotice(NoticeRequestDto noticeRequestDto);
    void deleteNotice(Long noticeId);
    Boolean editNotice(NoticeEditRequestDto noticeEditRequestDto);
    NoticePageResponseDto findRecentNotice(Integer pages);
    NoticeResponseDto findByNoticeId(Long noticeId);
}
