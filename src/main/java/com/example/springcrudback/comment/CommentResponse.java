package com.example.springcrudback.comment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record CommentResponse(Long id, Long postId, String content, String writer, String createdAt,
                              String updatedAt) {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(KST);

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getWriter(),
                format(comment.getCreatedAt()),
                format(comment.getUpdatedAt())
        );
    }

    private static String format(Instant instant) {
        return instant == null ? null : FORMATTER.format(instant);
    }
}