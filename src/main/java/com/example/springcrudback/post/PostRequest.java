package com.example.springcrudback.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRequest {

    private String title;
    private String content;

    public PostRequest() {
    }

    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}