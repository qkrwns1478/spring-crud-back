package com.example.springcrudback.post;

import com.example.springcrudback.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/view/posts")
@RequiredArgsConstructor
public class PostViewController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public String list() {
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("comments", commentService.findByPostId(id));
        return "posts/detail";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("postRequest", new PostRequest());
        return "posts/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PostResponse post = postService.findById(id);
        model.addAttribute("postRequest", new PostRequest(post.title(), post.content()));
        model.addAttribute("postId", id);
        return "posts/form";
    }
}