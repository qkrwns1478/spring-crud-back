package com.example.springcrudback.comment;

import com.example.springcrudback.post.Post;
import com.example.springcrudback.post.PostNotFoundException;
import com.example.springcrudback.post.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentResponse create(Long postId, CommentRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = new Comment(
                request.getContent(),
                username,
                post
        );

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    public List<CommentResponse> findByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        return commentRepository.findByPostIdOrderByIdAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    public void delete(Long postId, Long commentId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시글에 속한 댓글이 아닙니다.");
        }

        commentRepository.delete(comment);
    }

    public void deleteAllByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }
}