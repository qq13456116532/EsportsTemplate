package com.esports.esports.controller;

import com.esports.esports.service.AuthService;
import com.esports.esports.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final AuthService authService;   // ← 新增

    /** 是否收藏 */
    @GetMapping("/{productId}")
    public ResponseEntity<Boolean> isFavorited(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long productId) {
        Long uid = authService.currentUserId(authHeader);
        return ResponseEntity.ok(favoriteService.isFavorited(uid, productId));
    }

    /** 收藏列表 */
    @GetMapping
    public ResponseEntity<List<Long>> listFavorites(@RequestHeader("Authorization") String authHeader) {
        Long uid = authService.currentUserId(authHeader);
        return ResponseEntity.ok(favoriteService.listFavorites(uid));
    }

    /** 新增收藏 */
    @PostMapping
    public ResponseEntity<Void> addFavorite(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody FavoriteReq body) {
        if (body.productId() == null) return ResponseEntity.badRequest().build();
        Long uid = authService.currentUserId(authHeader);
        favoriteService.addFavorite(uid, body.productId());
        return ResponseEntity.ok().build();
    }

    /** 取消收藏 */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFavorite(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long productId) {
        Long uid = authService.currentUserId(authHeader);
        favoriteService.removeFavorite(uid, productId);
        return ResponseEntity.ok().build();
    }

    /** 仅含 productId 字段的简单 DTO */
    public record FavoriteReq(Long productId) { }
}
