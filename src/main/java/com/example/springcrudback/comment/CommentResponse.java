package com.example.springcrudback.comment;

import com.example.springcrudback.common.DateTimeUtils;

public record CommentResponse(Long id, Long postId, String content, String writer, String createdAt,
                              String updatedAt) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getWriter(),
                DateTimeUtils.format(comment.getCreatedAt()),
                DateTimeUtils.format(comment.getUpdatedAt())
        );
    }
}