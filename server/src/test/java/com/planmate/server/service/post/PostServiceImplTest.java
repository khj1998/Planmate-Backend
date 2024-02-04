package com.planmate.server.service.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.PostLike;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.PostLikeDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.repository.PostLikeRepository;
import lombok.With;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private PostLikeRepository postLikeRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8",true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("컬렉션 리펙토링 후 게시물 추가 테스트")
    @WithMockUser(roles = "USER")
    @Transactional
    void addPostTest() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setTitle("게시물 추가 테스트 코드");
        postDto.setContent("게시물 내용 테스트");

        List<String> postTagList = new ArrayList<>();
        postTagList.add("회계사");
        postTagList.add("감정평가사");
        postDto.setTagList(postTagList);

        String jsonValue = objectMapper.writeValueAsString(postDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/post/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+accessToken)
                .content(jsonValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Map<String,Object> responseJson = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        Object postId = responseJson.get("postId");

        mockMvc.perform(MockMvcRequestBuilders.get("/post/check")
                .param("postId", postId.toString())
                .header("Authorization","Bearer "+accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    @DisplayName("게시물 좋아요 중복 저장 테스트")
    @WithMockUser(roles = "USER")
    @Transactional
    @Rollback(false)
    void likeTest() throws Exception {
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

        List<PostLike> postLikeList = postLikeRepository.findAllByPostPostId(3L);

        mockMvc.perform(MockMvcRequestBuilders.post("/post/like")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+accessToken)
                .content(jsonValue))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<PostLike> postLikeList2 = postLikeRepository.findAllByPostPostId(3L);

        Assertions.assertThat(postLikeList.size()).isEqualTo(postLikeList2.size());
    }
}