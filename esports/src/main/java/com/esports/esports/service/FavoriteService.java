package com.esports.esports.service;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 纯 Redis 收藏功能实现, 双向收藏：
 *   fav:user:{uid}   -> Set<productId>
 *   fav:prod:{pid}   -> Set<userId>
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final StatefulRedisConnection<String, String> redis;

    private RedisCommands<String, String> cmd() {
        return redis.sync();
    }

    private static String userKey(Long uid) {
        return "fav:user:" + uid;
    }

    private static String productKey(Long pid) {
        return "fav:prod:" + pid;
    }

    /**
     * 添加收藏。
     *
     * @return true = 第一次收藏，false = 已收藏过
     */
    public boolean addFavorite(Long userId, Long productId) {
        long added = cmd().sadd(userKey(userId), productId.toString());
        cmd().sadd(productKey(productId), userId.toString()); // 双写，方便统计
        return added == 1;
    }

    /**
     * 取消收藏。
     *
     * @return true = 原本已收藏并成功删除，false = 原本就没收藏
     */
    public boolean removeFavorite(Long userId, Long productId) {
        long removed = cmd().srem(userKey(userId), productId.toString());
        cmd().srem(productKey(productId), userId.toString());
        return removed == 1;
    }

    /**
     * 判断是否已收藏。
     */
    public boolean isFavorited(Long userId, Long productId) {
        return cmd().sismember(userKey(userId), productId.toString());
    }

    /**
     * 用户收藏列表。
     */
    public List<Long> listFavorites(Long userId) {
        Set<String> ids = cmd().smembers(userKey(userId));
        return ids.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * 某商品被收藏的用户数。
     */
    public long countFavorites(Long productId) {
        return cmd().scard(productKey(productId));
    }
}