package com.planmate.server.controller;

import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.service.post.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시물 생성 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param postDto Body를 통해 전달받은 게시물 Dto 입니다.
     * @return 생성한 게시물 Dto를 Body에 포함해 반환합니다.
     */
    @PostMapping("/create")
    @ApiOperation("새 게시물을 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "새 게시물 생성 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "새 게시물을 생성하는데 실패함")
    })
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    /**
     * 태그를 통해 게시물 조회 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param tagName 쿼리 파라미터로 전달되는 태그입니다.
     * @return ResponseEntity<List<PostResponseDto>> 태그를 통해 조회된 게시물 Dto 리스트를 Body에 포함해 반환합니다.
     */
    @GetMapping("/find/with")
    @ApiOperation("태그로 게시물 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "태그로 게시물 조회 성공"),
            @ApiResponse(responseCode = "404",description = "태그로 게시물을 조회하는데 실패함")
    })
    public ResponseEntity<List<PostResponseDto>> findPostByTagName(@RequestParam("tagName") String tagName) {
        return ResponseEntity.ok(postService.findPostByTagName(tagName));
    }

    /**
     * Id를 통해 게시물 조회 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터를 통해 전달되는 게시물 Id 입니다.
     * @return ResponseEntity<PostResponseDto> Id를 통해 조회된 게시물 Dto를 Body에 포함해 반환합니다.
     */
    @GetMapping("/check")
    @ApiOperation("Id로 게시물 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 정상 조회"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "해당 Id를 가진 게시글이 없음")
    })
    public ResponseEntity<PostResponseDto> findPostById(@RequestParam("postId") Long postId) {
        return ResponseEntity.ok(postService.findByPostId(postId));
    }

    /**
     * Id를 통해 게시글 삭제 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터로 전달받는 게시물 Id입니다.
     * @return ResponseEntity 게시물 삭제 성공시 반환됩니다.
     */
    @DeleteMapping("/remove")
    @ApiOperation("Id로 게시물 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 정상 삭제"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "해당 Id를 가진 게시물이 없음")
    })
    public ResponseEntity deletePost(@RequestParam("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    /**
     * 본인이 작성한 게시물 조회 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @return ResponseEntity<List<PostResponseDto>> 조회된 작성한 게시물 Dto 리스트를 Body에 포함해 반환합니다.
     */
    @GetMapping("/find")
    @ApiOperation("내 게시물 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "나의 게시물 정상 조회"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "게시물 조회에 실패함")
    })
    public ResponseEntity<List<PostResponseDto>> findMyPost() {
        return ResponseEntity.ok(postService.findMyPost());
    }

    /**
     * 게시물 수정 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param postDto Body를 통해 전달되는 게시물 Dto 입니다.
     * @return ResponseEntity<PostResponseDto> 수정된 게시물 Dto를 Body에 포함해 반환합니다.
     */
    @PostMapping("/edit")
    @ApiOperation("게시물 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 수정 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "게시물 수정에 실패함")
    })
    public ResponseEntity<PostResponseDto> editPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.editPost(postDto));
    }

    /**
     * 게시물 스크랩 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param scrapDto Body를 통해 전달되는 스크랩된 게시물 Dto 입니다.
     * @return ResponseEntity<PostResponseDto> 수정된 게시물 Dto를 Body에 포함해 반환합니다.
     */
    @PostMapping("/scrap")
    @ApiOperation("게시물 스크랩")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 스크랩 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "게시물 스크랩에 실패함")
    })
    public ResponseEntity<PostResponseDto> scrapPost(@RequestBody ScrapDto scrapDto) {
        return ResponseEntity.ok(postService.scrapPost(scrapDto));
    }

    /**
     * 본인이 스크랩한 게시물 조회 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @return ResponseEntity<List<PostResponseDto>> 스크랩한 게시물 Dto 리스트를 Body에 포함해 반환합니다.
     */
    @GetMapping("/find/scrap")
    @ApiOperation("스크랩한 게시물 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "스크랩한 게시물 조회 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "스크랩한 게시물 조회에 실패함")
    })
    public ResponseEntity<List<PostResponseDto>> findScrapPost() {
        return ResponseEntity.ok(postService.findScrapPost());
    }

    /**
     * 게시물 스크랩 취소 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param postId 쿼리 파라미터를 통해 전달되는 게시물 Id입니다.
     * @return ResponseEntity 스크랩 삭제 성공시 반환됩니다.
     */
    @DeleteMapping("/remove/scrap")
    @ApiOperation("게시물 스크랩 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 스크랩 취소 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "해당 Id를 가진 게시물을 찾을 수 없음")
    })
    public ResponseEntity removeScrap(@RequestParam Long postId) {
        postService.deleteScrapById(postId);
        return ResponseEntity.ok().build();
    }
}
