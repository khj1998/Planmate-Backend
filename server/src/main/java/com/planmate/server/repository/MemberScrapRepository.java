package com.planmate.server.repository;

import com.planmate.server.domain.MemberScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberScrapRepository extends JpaRepository<MemberScrap,Long> {
    @Query("SELECT m from MemberScrap m " +
            "WHERE m.memberId = :ownerId AND m.postId  = :postId")
    Optional<MemberScrap> findMemberScrap(@Param("ownerId") Long ownerId, @Param("postId") Long postId);
    List<MemberScrap> findByMemberId(Long memberId);
    List<MemberScrap> findByPostId(Long postId);
}
