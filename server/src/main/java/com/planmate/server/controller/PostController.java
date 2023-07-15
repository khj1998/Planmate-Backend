package com.planmate.server.controller;

import com.planmate.server.domain.Post;
import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.PostLikeDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.dto.response.post.PostEditResponseDto;
import com.planmate.server.dto.response.post.PostPageResponseDto;
import com.planmate.server.dto.response.post.PostResponseDto;
import com.planmate.server.service.post.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/find/all")
    @ApiOperation("게시물 N개 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 최신 N개 조회 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "게시물 N개 조회하는데 실패함")
    })
    public ResponseEntity<PostPageResponseDto> findRecentPost(@RequestParam("pages") Integer pages) {
        PostPageResponseDto responseDto = postService.findRecentPost(pages);
        return ResponseEntity.ok(responseDto);
    }

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
            @ApiResponse(responseCode = "404",description = "새 게시물 생성에 실패함")
    })
    public ResponseEntity<PostCreateResponseDto> createPost(@RequestBody PostDto postDto) {
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
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "태그로 게시물 조회 실패함")
    })
    public ResponseEntity<PostPageResponseDto> findPostByTagName(@RequestParam("tagName") String tagName,@RequestParam("pages") Integer pages) {
        return ResponseEntity.ok(postService.findPostByTagName(tagName,pages));
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
            @ApiResponse(responseCode = "404",description = "게시물 조회에 실패함")
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
            @ApiResponse(responseCode = "404",description = "게시물 삭제에 실패함")
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
            @ApiResponse(responseCode = "404",description = "내 게시물 조회에 실패함")
    })
    public ResponseEntity<PostPageResponseDto> findMyPost(@RequestParam Integer pages) {
        return ResponseEntity.ok(postService.findMyPost(pages));
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
    public ResponseEntity<PostEditResponseDto> editPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.editPost(postDto));
    }

    /**
     * 게시물 스크랩 요청을 받습니다.
     * @author kimhojin98@naver.com
     * @param scrapDto Body를 통해 전달되는 스크랩된 게시물 Dto 입니다.
     * @return ResponseEntity<PostResponseDto> 수정된 게시물 Dto를 Body에 포함해 반환합니다.
     */
    @PostMapping("/scrap")
    @ApiOperation("게시물 스크랩 추가/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 스크랩 추가/취소 성공"),
            @ApiResponse(responseCode = "401",description = "해당 사용자가 인증되지 않음 | 토큰 만료"),
            @ApiResponse(responseCode = "403",description = "해당 사용자가 Member 권한이 아님"),
            @ApiResponse(responseCode = "404",description = "게시물 스크랩 추가/취소에 실패함")
    })
    public ResponseEntity<Boolean> scrapPost(@RequestBody ScrapDto scrapDto) {
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
    public ResponseEntity<PostPageResponseDto> findScrapPost(@RequestParam Integer pages) {
        return ResponseEntity.ok(postService.findScrapPost(pages));
    }

    @PostMapping("/like")
    @ApiOperation("게시물에 좋아요 추가/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "게시물 좋아요 추가/취소 성공")
    })
    public ResponseEntity<Boolean> setPostLike(@RequestBody PostLikeDto postLikeDto) {
        return ResponseEntity.ok(postService.setPostLike(postLikeDto.getPostId()));
    }
}
