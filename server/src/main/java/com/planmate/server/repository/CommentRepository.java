package com.planmate.server.repository;

import com.planmate.server.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.member.memberId = :memberId and "
          +"c.id = :commentId")
    Optional<Comment> findComment(@Param("memberId") Long memberId,@Param("commentId") Long commentId);

    @Query("select c from Comment c where c.parentCommentId = :parentId")
    List<Comment> findAllChildComment(@Param("parentId") Long parentId);

    @EntityGraph(value = "comment_paging")
    @Query("select c from Comment c where c.post.postId = :postId and "
          +"c.parentCommentId = null")
    Page<Comment> findRecentComment(@Param("postId") Long postId, Pageable pageable);

    @EntityGraph(value = "comment_paging")
    @Query("select c from Comment c where c.post.postId = :postId and "
            +"c.parentCommentId = :parentId")
    List<Comment> findChildRecent(@Param("postId") Long postId,@Param("parentId") Long parentId);

    @EntityGraph(value = "comment_paging")
    @Query("select c from Comment c where c.member.memberId = :memberId")
    Page<Comment> findAllByMemberId(@Param("memberId") Long memberId,Pageable pageable);
}
