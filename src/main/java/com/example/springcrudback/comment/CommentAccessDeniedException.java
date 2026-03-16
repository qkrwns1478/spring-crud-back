package com.example.springcrudback.comment;

public class CommentAccessDeniedException extends RuntimeException {

    public CommentAccessDeniedException() {
        super("해당 댓글에 대한 권한이 없습니다.");
    }
}