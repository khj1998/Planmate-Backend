package com.planmate.server.controller;

import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.token.RefreshTokenDto;
import com.planmate.server.repository.TokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TokenControllerTest {
    @LocalServerPort
    private int port;

    private RestTemplate testRestTemplate;
    @Autowired
    private TokenRepository tokenRepository;
    private HttpEntity request;
    private Token token;
    private HttpHeaders httpHeaders;

    public TokenControllerTest() {}

    @BeforeEach
    public void prepare() {
        testRestTemplate = new RestTemplate();
        token = tokenRepository.findById(7L).orElseThrow();
        httpHeaders = new HttpHeaders();
        String header = "Bearer " + token.getAccessToken();
        httpHeaders.set("Authorization", header);
    }


    @Test
    void checkExpiredAt() {
        //given
        String url = "http://localhost:" + port + "/token/expired_at";
        request = new HttpEntity(httpHeaders);
        //when
        ResponseEntity<Boolean> response = testRestTemplate.exchange(url, HttpMethod.GET, request, Boolean.class);
        //then
        assertThat(response.getBody()).isEqualTo(false);
    }

    @Test
    void reissueAccessToken() {
        //given
        String url = "http://localhost:" + port + "/token/refresh";
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();

        refreshTokenDto.setId(token.getMemberId());
        refreshTokenDto.setAccessToken(token.getAccessToken());
        refreshTokenDto.setRefreshToken(token.getRefreshToken());

        request = new HttpEntity(refreshTokenDto, httpHeaders);
        //when
        ResponseEntity<Token> response = testRestTemplate.exchange(url, HttpMethod.POST, request, Token.class);
        //then
        assertThat(response.getBody().getAccessTokenExpiredAt()).isEqualTo(LocalDate.now().plusDays(60));
    }
}