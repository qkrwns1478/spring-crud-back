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

    public CommentResponse update(Long postId, Long commentId, CommentRequest request, String username) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateCommentBelongsToPost(comment, postId);
        validateWriter(comment, username);

        comment.setContent(request.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return CommentResponse.from(updatedComment);
    }

    public void delete(Long postId, Long commentId, String username) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateCommentBelongsToPost(comment, postId);
        validateWriter(comment, username);

        commentRepository.delete(comment);
    }

    public void deleteAllByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
    }

    private void validateCommentBelongsToPost(Comment comment, Long postId) {
        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시글에 속한 댓글이 아닙니다.");
        }
    }

    private void validateWriter(Comment comment, String username) {
        if (!comment.getWriter().equals(username)) {
            throw new CommentAccessDeniedException();
        }
    }
}