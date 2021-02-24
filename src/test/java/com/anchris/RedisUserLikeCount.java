package com.anchris;

import com.anchris.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Community01Application.class)
public class RedisUserLikeCount {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testUserLikeCount(){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(11);
        System.err.println(userLikeKey);
        redisTemplate.opsForValue().set(userLikeKey,"22");
        Integer count =   (Integer) redisTemplate.opsForValue().get(userLikeKey);

        if(count==null)
            System.err.println("count为空");
        else
         System.err.println(count);
    }
}
