package com.esports.esports.service;

import com.esports.esports.model.Comment;
import com.esports.esports.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /** 查询指定商品的全部评论（按时间倒序） */
    public List<Comment> getCommentsByProduct(Long productId) {
        return commentRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    /** 简易添加接口 */
    public Comment addComment(Comment c) {
        return commentRepository.save(c);
    }
}
