package com.planmate.server.service.post;

import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostResponseDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostDto postDto);
    PostResponseDto findByPostId(Long postId);
    void deletePost(Long postId);
    void deleteScrapById(Long postId);
    List<PostResponseDto> findMyPost();
    PostResponseDto editPost(PostDto postDto);
    PostResponseDto scrapPost(ScrapDto scrapDto);
    List<PostResponseDto> findScrapPost();
    List<PostResponseDto> findPostByTagName(String tagName);
}
