package com.planmate.server.controller;

import com.planmate.server.dto.response.login.LoginResponseDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.planmate.server.domain.Member;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.s3.S3UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/member")
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"사용자 API"})
public class MemberController {
    private final MemberService memberService;
    private final S3UploadService s3UploadService;

    /**
     * TODO: log-out
     * */
    @GetMapping("info")
    @ApiOperation(value = "사용자 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 환료")
    })
    public ResponseEntity<Member> getInfo() {
        return ResponseEntity.ok(memberService.getInfo());
    }

    @DeleteMapping("sign-out")
    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 환료")
    })
    public ResponseEntity signOut() {
        memberService.signOut();
        return ResponseEntity.ok().build();
    }

    @GetMapping("name")
    @ApiOperation(value = "회원 이름 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이름 변경 환료")
    })
    public ResponseEntity<Member> modifyName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(memberService.modifyName(name));
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 변경 환료")
    })
    @PostMapping(value = "/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Member> modifyImg(@RequestParam(value = "image")MultipartFile multipartFile) throws IOException {
        String img = s3UploadService.upload(multipartFile, "profile");

        return ResponseEntity.ok(memberService.modifyImg(img));
    }
}
