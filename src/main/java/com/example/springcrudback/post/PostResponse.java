package com.example.springcrudback.post;

import com.example.springcrudback.common.DateTimeUtils;

public record PostResponse(Long id, String title, String content, String createdAt, String updatedAt) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                DateTimeUtils.format(post.getCreatedAt()),
                DateTimeUtils.format(post.getUpdatedAt())
        );
    }
}