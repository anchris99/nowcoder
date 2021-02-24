package com.anchris.service;

import com.anchris.Community01Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Community01Application.class)
public class FollowServiceTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    FollowService followService;
    private static final int userId=111;
    private static final int entityType=1;
    private static final int entityId=222;

    @Test
    public void test1(){
//        followService.follow(userId,entityType,entityId);
        followService.unfollow(userId,entityType,entityId);
        System.out.println("用户"+ userId+"关注数量"+followService.findFolloweeCount(userId,entityType));
        System.out.println("实体被关注数量"+followService.findFollowerCount(entityType,entityId));
        System.out.println(followService.hasFollowed(userId,entityType,entityId));
    }

    @Test
    public void test2(){
        List<Map<String, Object>> followers = followService.findFollowers(111, 0, 100);
        System.err.println(followers.size());
        System.err.println(followService.findFollowerCount(3, 111));

    }
}
