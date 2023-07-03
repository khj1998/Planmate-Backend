package com.planmate.server.repository;

import com.planmate.server.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {

    @Query("select l from CommentLike l where l.commentId = :commentId")
    List<CommentLike> findAllByCommentId(@Param("commentId") Long commentId);

    @Query("select l from CommentLike l where l.userId = :userId and "+
          "l.commentId = :commentId")
    CommentLike findCommentLike(@Param("userId") Long userId,
                                          @Param("commentId") Long commentId);
}
