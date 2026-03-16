package com.example.springcrudback.comment;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long commentId) {
        super("해당 댓글이 없습니다. id=" + commentId);
    }
}