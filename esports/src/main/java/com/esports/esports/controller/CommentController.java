package com.esports.esports.controller;

import com.esports.esports.model.Comment;
import com.esports.esports.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /** GET /comments?productId=123 */
    @GetMapping
    public ResponseEntity<List<Comment>> list(@RequestParam Long productId) {
        return ResponseEntity.ok(commentService.getCommentsByProduct(productId));
    }

    /** POST /comments  （需要登录态，这里仅演示） */
    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }
}
