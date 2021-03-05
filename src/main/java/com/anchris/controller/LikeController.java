package com.anchris.controller;

import com.anchris.entity.Event;
import com.anchris.entity.User;
import com.anchris.event.EventProducer;
import com.anchris.service.LikeService;
import com.anchris.util.CommunityConstant;
import com.anchris.util.CommunityUtil;
import com.anchris.util.HostHolder;
import com.anchris.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class LikeController  implements CommunityConstant {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId,int postId){
        User user = hostHolder.getUser();
        //点赞 取消点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("likeStatus",likeStatus);
        map.put("likeCount",likeCount);

        if(likeStatus==1){
            Event event =new Event();
            event.setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);

            eventProducer.fireEvent(event);

            //计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, postId);

        }
        return CommunityUtil.getJSONString(0,null,map);
    }
}
