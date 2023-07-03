package com.planmate.server.repository;

import com.planmate.server.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    List<PostLike> findByPostId(Long postId);
}
