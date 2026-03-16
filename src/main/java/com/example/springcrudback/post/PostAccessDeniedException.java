package com.example.springcrudback.post;

public class PostAccessDeniedException extends RuntimeException {

    public PostAccessDeniedException() {
        super("해당 게시글에 대한 권한이 없습니다.");
    }
}