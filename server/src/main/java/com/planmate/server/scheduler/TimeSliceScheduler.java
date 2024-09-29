package com.planmate.server.scheduler;

import com.planmate.server.service.subject.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeSliceScheduler {
    private final SubjectService subjectService;

    @Scheduled(cron = "0 0 6,12,18 * * ?")
    public void runSliceScheduler() {
        LocalDateTime now = LocalDateTime.now();

        log.info("==========각 유저 당일 {} 시까지 총 공부량 백업 시작==========", now.getHour());
        subjectService.backupTimeSlice();
        log.info("==========각 유저 당일 {} 시까지 총 공부량 백업 완료==========", now.getHour());
    }
}
