package com.planmate.server.scheduler;

import com.planmate.server.service.subject.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DayScheduler {

    private final SubjectService subjectService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runDayScheduler() {
        log.info("==========공부 시간 초기화 스케줄러 기동==========");
        subjectService.backUpAndInit();
        log.info("==========공부 시간 초기화 스케줄러 완료==========");
    }
}
