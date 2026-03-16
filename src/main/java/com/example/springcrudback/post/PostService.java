package com.example.springcrudback.post;

import com.example.springcrudback.comment.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public PostResponse create(PostRequest request, String username) {
        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                username
        );

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return PostResponse.from(post);
    }

    public PostResponse update(Long id, PostRequest request, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        validateWriter(post, username);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Post updatedPost = postRepository.save(post);
        return PostResponse.from(updatedPost);
    }

    @Transactional
    public void delete(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        validateWriter(post, username);

        commentRepository.deleteByPostId(id);
        postRepository.delete(post);
    }

    private void validateWriter(Post post, String username) {
        if (!post.getWriter().equals(username)) {
            throw new PostAccessDeniedException();
        }
    }
}