package com.planmate.server.controller;

import com.planmate.server.domain.Token;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostControllerTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberScrapRepository memberScrapRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private TokenRepository tokenRepository;
    private HttpEntity request;
    private HttpHeaders httpHeaders;
    private Token token;

    public PostControllerTest() {}

    @BeforeEach
    void init() {
        restTemplate = new RestTemplate();
        token = tokenRepository.findById(8L).orElseThrow();
        httpHeaders = new HttpHeaders();
        String header = "Bearer "+token.getAccessToken();
        httpHeaders.set("Authorization",header);
    }

    @Test
    void createPost() {
        //given
        List<String> tagList = new ArrayList<>();
        String url = "http://localhost:"+port+"/create";
        PostDto postDto = new PostDto();
        postDto.setTagList(tagList);
        postDto.setTitle("createPost test");
        postDto.setContent("By 호진");
        request = new HttpEntity(postDto,httpHeaders);
        //when
        ResponseEntity<PostResponseDto> response = restTemplate.exchange(url, HttpMethod.POST,request,PostResponseDto.class);
        //then
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo(postDto.getTitle());
    }

    @Test
    void findPostByTagName() {
        String url ="http://localhost:"+port+"/find/with?tagName=#개발";

        ResponseEntity<PostResponseDto[]> response = restTemplate.exchange(url,HttpMethod.GET,request,PostResponseDto[].class);
        PostResponseDto postResponseDto = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .findFirst().orElse(null);

        Assertions.assertThat(postResponseDto.getPostTagList()).contains("#개발");
    }

    @Test
    void findPostById() {
        String url ="http://localhost:"+port+"/check?postId=15";
        request = new HttpEntity(httpHeaders);

        ResponseEntity<PostResponseDto> response = restTemplate.exchange(url,HttpMethod.GET,request,PostResponseDto.class);

        Assertions.assertThat(response.getBody().getPostId()).isEqualTo(15L);
    }

    @Test
    void deletePost() {
        String url ="http://localhost:"+port+"/remove?postId=14";
        request = new HttpEntity(httpHeaders);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.DELETE,request,ResponseEntity.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findMyPost() {
        //given
        String url = "http://localhost:"+port+"/find";
        request = new HttpEntity(httpHeaders);
        //when
        ResponseEntity<PostResponseDto[]> response = restTemplate.exchange(url,HttpMethod.GET,request,PostResponseDto[].class);
        PostResponseDto postResponseDto = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .findFirst().orElse(null);
        //then
        Assertions.assertThat(postResponseDto.getPostId()).isEqualTo(15L);
    }

    @Test
    void editPost() {
        //given
        String url = "http://localhost:"+port+"/edit";
        PostDto postDto = new PostDto();
        postDto.setId(15L);
        postDto.setTitle("createPost test");
        postDto.setContent("By 승언");
        request = new HttpEntity(postDto,httpHeaders);
        //when
        ResponseEntity<PostResponseDto> response = restTemplate.exchange(url,HttpMethod.POST,request, PostResponseDto.class);
        //then
        Assertions.assertThat(response.getBody().getTitle()).isEqualTo("createPost test");
        Assertions.assertThat(response.getBody().getContent()).isEqualTo("By 승언");
    }

    @Test
    void scrapPost() {
        String url = "http://localhost:"+port+"/scrap";
        ScrapDto scrapDto = new ScrapDto();
        scrapDto.setPostId(12L);
        request = new HttpEntity(scrapDto,httpHeaders);

        ResponseEntity<PostResponseDto> response = restTemplate.exchange(url,HttpMethod.POST,request, PostResponseDto.class);

        Assertions.assertThat(response.getBody().getPostId()).isEqualTo(12L);
    }

    @Test
    void findScrapPost() {
        String url = "http://localhost:"+port+"/find/scrap";
        request = new HttpEntity(httpHeaders);

        ResponseEntity<PostResponseDto[]> response = restTemplate.exchange(url,HttpMethod.GET,request,PostResponseDto[].class);
        PostResponseDto postResponseDto = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .findFirst().orElse(null);

        Assertions.assertThat(postResponseDto.getPostId()).isEqualTo(11L);
    }

    @Test
    void removeScrap() {
        String url ="http://localhost:"+port+"/remove/scrap?postId=12";
        request = new HttpEntity(httpHeaders);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.DELETE,request, ResponseEntity.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}