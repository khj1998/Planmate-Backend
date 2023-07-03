package com.planmate.server.repository;

import com.planmate.server.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    @Query("select pl from PostLike pl where pl.memberId = :memberId and "+
           "pl.postId = :postId")
    PostLike findByPost(@Param("memberId") Long memberId,@Param("postId") Long postId);
    List<PostLike> findAllByPostId(Long postId);
}
