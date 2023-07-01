package com.planmate.server.repository;

import com.planmate.server.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag,Long> {
    List<PostTag> findByTagName(String tagName);
}
