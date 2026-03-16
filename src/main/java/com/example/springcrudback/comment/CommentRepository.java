package com.example.springcrudback.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByIdAsc(Long postId);

    @Transactional
    void deleteByPostId(Long postId);
}