package com.planmate.server.service.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.dto.request.post.PostLikeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostServiceImplTest {
    private MockMvc mockMvc;

    @Value("${access_token}")
    private String accessToken;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8",true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }
    
    @Test
    @DisplayName("게시물 좋아요 중복 저장 테스트")
    @WithMockUser(roles = "USER")
    @Transactional
    @Rollback(false)
    void like_test() throws Exception {
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setPostId(3L);
        String jsonValue = objectMapper.writeValueAsString(postLikeDto);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for ( int i=0;i<2;i++) {
            executorService.execute(() -> {
                try {
                    mockMvc.perform(MockMvcRequestBuilders.post("/post/like")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization","Bearer "+accessToken)
                                    .content(jsonValue))
                            .andExpect(MockMvcResultMatchers.status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    @WithMockUser(roles = "USER")
    @Transactional
    @Rollback(false)
    void check_post_like() throws Exception {
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setPostId(3L);
        String jsonValue = objectMapper.writeValueAsString(postLikeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/like")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+accessToken)
                .content(jsonValue))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}