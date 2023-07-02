package com.planmate.server.service.tendinous;

import com.planmate.server.dto.request.tendinous.AlertRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {
    private final RestTemplate restTemplate;
    private final String url;

    @Override
    public void alert(final AlertRequestDto alertRequestDto) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", "spring-bot");
        request.put("text", alertRequestDto.toString()); //전송할 메세지

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
