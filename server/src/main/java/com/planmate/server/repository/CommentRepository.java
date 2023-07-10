package com.planmate.server.repository;

import com.planmate.server.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.memberId = :memberId and "
          +"c.id = :commentId")
    Optional<Comment> findComment(@Param("memberId") Long memberId,@Param("commentId") Long commentId);

    @Query("select c from Comment c where c.postId = :postId")
    List<Comment> findRecentComment(@Param("postId") Long postId, Pageable pageable);

    List<Comment> findAllByMemberId(Long memberId);
}
