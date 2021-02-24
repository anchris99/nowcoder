package com.anchris.controller;

import com.anchris.entity.User;
import com.anchris.service.LikeService;
import com.anchris.util.CommunityUtil;
import com.anchris.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){
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
        return CommunityUtil.getJSONString(0,null,map);
    }
}
