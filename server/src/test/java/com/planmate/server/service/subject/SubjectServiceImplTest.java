package com.planmate.server.service.subject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planmate.server.domain.Member;
import com.planmate.server.domain.StudyTimeSlice;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.repository.MemberRepository;
import com.planmate.server.repository.StudyTimeSliceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SubjectServiceImplTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StudyTimeSliceRepository studyTimeSliceRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8",true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("특정 시간마다 유저 공부시간 백업 테스트")
    @Transactional
    @Rollback(false)
    void addPostTest() throws Exception {
        List<Member> memberList = memberRepository.findAll();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/time/slice/backup")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<StudyTimeSlice> studyTimeSliceList = studyTimeSliceRepository.findAll();

        Assertions.assertThat(memberList.size()).isEqualTo(studyTimeSliceList.size());
    }
}