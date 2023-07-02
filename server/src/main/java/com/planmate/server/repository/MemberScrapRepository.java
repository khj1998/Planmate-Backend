package com.planmate.server.repository;

import com.planmate.server.domain.MemberScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberScrapRepository extends JpaRepository<MemberScrap,Long> {
    @Query("SELECT m from MemberScrap m " +
            "WHERE m.owner.memberId = :ownerId AND m.post.postId  = :postId")
    MemberScrap findByOwnerMemberIdAndPostPostId(@Param("ownerId") Long ownerId,@Param("postId") Long postId);
    List<MemberScrap> findByOwnerMemberId(Long memberId);
}
