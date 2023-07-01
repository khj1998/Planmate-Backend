package com.planmate.server.repository;

import com.planmate.server.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByOwnerMemberId(Long ownerId);
    Optional<Post> findByPostIdAndOwnerMemberId(Long postId, Long ownerId);
}
