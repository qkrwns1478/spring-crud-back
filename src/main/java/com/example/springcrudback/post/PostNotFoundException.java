package com.example.springcrudback.post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Post with id " + id + " does not exist");
    }
}