package com.example.springcrudback.post;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record PostResponse(Long id, String title, String content, String createdAt, String updatedAt) {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(KST);

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                format(post.getCreatedAt()),
                format(post.getUpdatedAt())
        );
    }

    private static String format(Instant instant) {
        return instant == null ? null : FORMATTER.format(instant);
    }

}