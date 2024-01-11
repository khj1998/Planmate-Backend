package com.planmate.server.config;

import com.planmate.server.repository.*;
import com.planmate.server.service.comment.CommentService;
import com.planmate.server.service.comment.CommentServiceImpl;
import com.amazonaws.services.s3.AmazonS3;
import com.planmate.server.service.logout.LogOutService;
import com.planmate.server.service.logout.LogOutServiceImpl;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.member.MemberServiceImpl;
import com.planmate.server.service.notice.NoticeService;
import com.planmate.server.service.notice.NoticeServiceImpl;
import com.planmate.server.service.planner.PlannerService;
import com.planmate.server.service.planner.PlannerServiceImpl;
import com.planmate.server.service.post.PostService;
import com.planmate.server.service.post.PostServiceImpl;
import com.planmate.server.service.statistic.StatisticService;
import com.planmate.server.service.statistic.StatisticServiceImpl;
import com.planmate.server.service.subject.SubjectService;
import com.planmate.server.service.subject.SubjectServiceImpl;
import com.planmate.server.service.s3.S3UploadService;
import com.planmate.server.service.s3.S3UploaderServiceImpl;
import com.planmate.server.service.schedule.ScheduleService;
import com.planmate.server.service.schedule.ScheduleServiceImpl;
import com.planmate.server.service.tendinous.AlertService;
import com.planmate.server.service.tendinous.AlertServiceImpl;
import com.planmate.server.service.token.TokenService;
import com.planmate.server.service.token.TokenServiceImpl;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Generated
/**
 * @author 지승언
 * service 로직에 대한 bean을 직접 등록하는 파일이다
 * 직접 등록하는 이유는 나중에 서비스 교체할 일이 있을 경우와 유지보수를 위해서이다
 * */
@Configuration
public class SpringConfig {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PostRepository postRepository;
    private PostTagRepository postTagRepository;
    private final MemberScrapRepository memberScrapRepository;
    private final SubjectRepository subjectRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3 amazonS3Client;
    private final ScheduleRepository scheduleRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final PlannerRepository plannerRepository;
    private final StudyBackUpRepository studyBackUpRepository;

    @Autowired
    public SpringConfig(final MemberRepository memberRepository,
                        final TokenRepository tokenRepository,
                        final PostRepository postRepository,
                        final PostTagRepository postTagRepository,
                        final SubjectRepository subjectRepository,
                        final CommentRepository commentRepository,
                        final MemberScrapRepository memberScrapRepository,
                        final AmazonS3 amazonS3Client,
                        final ScheduleRepository scheduleRepository,
                        final CommentLikeRepository commentLikeRepository,
                        final PostLikeRepository postLikeRepository,
                        final PlannerRepository plannerRepository,
                        final StudyBackUpRepository studyBackUpRepository) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
        this.memberScrapRepository = memberScrapRepository;
        this.subjectRepository = subjectRepository;
        this.commentRepository = commentRepository;
        this.amazonS3Client = amazonS3Client;
        this.scheduleRepository = scheduleRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.postLikeRepository = postLikeRepository;
        this.plannerRepository = plannerRepository;
        this.studyBackUpRepository = studyBackUpRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository, tokenRepository);
    }

    @Bean
    public TokenService tokenService() {
        return new TokenServiceImpl(tokenRepository, memberRepository);
    }

    @Bean
    public PostService postService() {
        return new PostServiceImpl(postRepository,postTagRepository,memberRepository,memberScrapRepository,postLikeRepository,commentRepository);
    }

    @Bean
    public SubjectService subjectService() {
        return new SubjectServiceImpl(subjectRepository,studyBackUpRepository);
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceImpl(postRepository,commentRepository, memberRepository,commentLikeRepository);
    }

    @Bean
    public S3UploadService S3UploadService() {
        return new S3UploaderServiceImpl(amazonS3Client);
    }

    @Bean
    public ScheduleService scheduleService() {
        return new ScheduleServiceImpl(scheduleRepository);
    }

    @Bean
    public LogOutService logOutService() {
        return new LogOutServiceImpl(tokenRepository, memberRepository);
    }

    @Bean
    public StatisticService statisticService() {
        return new StatisticServiceImpl(subjectRepository, studyBackUpRepository);
    }

    @Bean
    public PlannerService plannerService() {
        return new PlannerServiceImpl(plannerRepository);
    }

    @Bean
    public NoticeService noticeService() {
        return new NoticeServiceImpl(postRepository,memberRepository,memberScrapRepository,postLikeRepository,commentRepository);
    }
}