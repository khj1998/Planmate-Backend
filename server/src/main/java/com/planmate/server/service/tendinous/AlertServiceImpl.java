package com.planmate.server.service.tendinous;

import com.planmate.server.dto.request.tendinous.AlertRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final RestTemplate restTemplate;

    @Override
    public void alert(final AlertRequestDto alertRequestDto) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", "spring-bot");
        request.put("text", alertRequestDto.toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

        restTemplate.exchange("http://localhost:3000", HttpMethod.POST, entity, String.class);
    }
}
