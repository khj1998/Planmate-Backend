package com.planmate.server.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@Slf4j
@Api(tags = {"스케줄 관련 API"})
public class ScheduleController {
}
