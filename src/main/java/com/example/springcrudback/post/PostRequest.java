package com.example.springcrudback.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRequest {

    @NotBlank(message = "제목은 비어 있을 수 없습니다.")
    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다.")
    @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
    private String content;

    public PostRequest() {
    }

    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}