package com.example.springcrudback.post;

import com.example.springcrudback.common.util.DateTimeUtils;

public record PostResponse(Long id, String title, String content, String writer, String createdAt, String updatedAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getWriter(),
                DateTimeUtils.format(post.getCreatedAt()),
                DateTimeUtils.format(post.getUpdatedAt())
        );
    }
}