package com.example.springcrudback.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController @RequestMapping("/posts")
public class PostController {
    private final Map<Long, Post> postsStore = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody PostRequest request) {
        Long id = sequence.getAndIncrement();
        Post post = new Post(id, request.getTitle(), request.getContent());
        postsStore.put(id, post);
        return post;
    }

    @GetMapping @ResponseStatus(HttpStatus.OK)
    public List<Post> getPosts() {
        return new ArrayList<>(postsStore.values());
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        Post post = postsStore.get(id);
        if (post == null) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        return post;
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        Post post = postsStore.get(id);
        if (post == null) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        return post;
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        Post post = postsStore.get(id);
        if (post == null) {
            throw new IllegalArgumentException("Post with id " + id + " does not exist");
        }
        postsStore.remove(id);
    }
}
