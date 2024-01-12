package com.planmate.server.service.post;

import com.planmate.server.dto.request.post.PostDto;
import com.planmate.server.dto.request.post.ScrapDto;
import com.planmate.server.dto.response.post.PostCreateResponseDto;
import com.planmate.server.dto.response.post.PostEditResponseDto;
import com.planmate.server.dto.response.post.PostPageResponseDto;
import com.planmate.server.dto.response.post.PostResponseDto;

import java.util.List;

public interface PostService {
    PostCreateResponseDto createPost(PostDto postDto);
    void deletePost(Long postId);
    PostPageResponseDto findRecentPost(Integer pages);
    PostPageResponseDto findPostByTagName(String tagName,Integer pages);
    PostPageResponseDto findMyPost(Integer pages);
    PostEditResponseDto editPost(PostDto postDto);
    Boolean scrapPost(ScrapDto scrapDto);
    PostPageResponseDto findScrapPost(Integer pages);
    Boolean setPostLike(Long postId);
}
