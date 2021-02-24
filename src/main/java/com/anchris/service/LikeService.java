package com.anchris.service;

import com.anchris.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    RedisTemplate redisTemplate;
    //点赞

    public void like(int userId,int entityType,int entityId,int entityUserId){
        //点赞和收到点赞要保持一致
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                Boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);

                //Mark the start of a transaction block.
                operations.multi();

                if(isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                //Executes all queued commands in a transaction started with multi().
               return operations.exec();
            }
        });
    }
    //查询实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        return redisTemplate.opsForSet().size(RedisKeyUtil.getEntityLikeKey(entityType,entityId));
    }

    //查询点赞状态 1点赞， 0未点赞
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1:0;
    }

    //查询用户获赞数量
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count==null?0:count;

    }
}
