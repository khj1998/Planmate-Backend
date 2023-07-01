package com.planmate.server.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authority")
@Slf4j
@Api(tags = {"권한 관련 API"})
public class AuthorityController {
}
