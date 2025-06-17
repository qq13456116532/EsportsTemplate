package com.esports.esports.controller;

import com.esports.esports.model.Comment;
import com.esports.esports.service.AuthService;
import com.esports.esports.service.CommentService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /** GET /comments?productId=123 */
    @GetMapping
    public ResponseEntity<List<CommentDTO>> list(@RequestParam Long productId) {
        List<Comment> comments = commentService.getCommentsByProduct(productId);
        List<CommentDTO> res = comments.stream().map(c -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(c.getId());
            dto.setUsername(c.getUser().getNickName());
            dto.setAvatar(c.getUser().getAvatarUrl());
            dto.setContent(c.getContent());
            dto.setRating(c.getRating());
            dto.setTimestamp(c.getCreatedAt().toString()); // 这里可以格式化一下
            // dto.setProductName(c.getProduct().getName()); // 如有需要
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    private final AuthService authService;   // ← 新增
    
    public record AddCommentReq(Long orderId, Long productId, String content, int rating) {}
    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody AddCommentReq req, @RequestHeader("Authorization") String authHeader) {
        Long uid = authService.currentUserId(authHeader);
        return ResponseEntity.ok(commentService.addComment(uid, req));
    }

}
@Data
class CommentDTO {
    private Long id;
    private String username;
    private String avatar;
    private String content;
    private Integer rating;
    private String timestamp;
    // 如果你需要 product 的简单字段，也可以加上，比如 productId, productName
}