package com.planmate.server.repository;

import com.planmate.server.domain.Post;
import com.planmate.server.domain.PostTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long> {
    Page<PostTag> findByTagName(String tagName, Pageable pageable);
    List<PostTag> findByPostId(Long postId);
}
