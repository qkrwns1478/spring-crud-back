package com.example.springcrudback.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequest {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    @Size(max = 300, message = "댓글은 300자 이하여야 합니다.")
    private String content;

    public CommentRequest() {
    }

}