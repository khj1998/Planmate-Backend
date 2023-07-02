package com.planmate.server.controller;

import com.planmate.server.domain.Authority;
import com.planmate.server.domain.Member;
import com.planmate.server.service.member.MemberService;
import com.planmate.server.service.s3.S3UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            @ApiResponse(responseCode = "200", description = "조회 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<Member> getInfo() {
        return ResponseEntity.ok(memberService.getInfo());
    }

    @DeleteMapping("sign-out")
    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity signOut() {
        memberService.signOut();
        return ResponseEntity.ok().build();
    }

    @GetMapping("name")
    @ApiOperation(value = "회원 이름 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이름 변경 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    public ResponseEntity<Member> modifyName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(memberService.modifyName(name));
    }

    @ApiOperation(value = "프로필 이미지 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 변경 환료"),
            @ApiResponse(responseCode = "401", description = "토큰 만료"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "해당 멤버 없음"),
    })
    @PostMapping(value = "/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Member> modifyImg(@RequestParam(value = "image")MultipartFile multipartFile) throws IOException {
        String img = s3UploadService.upload(multipartFile, "profile");

        return ResponseEntity.ok(memberService.modifyImg(img));
    }
}
