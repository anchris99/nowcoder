package com.anchris.controller;

import com.anchris.annotation.LoginRequired;
import com.anchris.entity.Event;
import com.anchris.entity.Page;
import com.anchris.entity.User;
import com.anchris.event.EventProducer;
import com.anchris.service.FollowService;
import com.anchris.service.UserService;
import com.anchris.util.CommunityConstant;
import com.anchris.util.CommunityUtil;
import com.anchris.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController  implements CommunityConstant{
    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @LoginRequired
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);
        // 触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);
        return CommunityUtil.getJSONString(0, "已关注!");
    }

    @LoginRequired
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注!");
    }

    @LoginRequired
    @RequestMapping(path = "/followees/{userId}",method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId")int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if(user==null)
            throw new RuntimeException("user == null");
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/"+userId);
        long followeeCount = followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER);
        page.setRows((int)followeeCount);

        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        //向每项数据中添加是否 关注对方的信息
        if(followees!=null){
            for (Map<String, Object> followee : followees) {
                User u = (User) followee.get("user");
                followee.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",followees);
        return "/site/followee";
    }


    @LoginRequired
    @RequestMapping(path = "/followers/{userId}",method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId")int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if(user==null)
            throw new RuntimeException("user == null");
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/"+userId);
        long followerCount = followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER,userId);
        page.setRows((int)followerCount);

        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        //向每项数据中添加是否 关注对方的信息
        if(followers!=null){
            for (Map<String, Object> follower : followers) {
                User u = (User) follower.get("user");
                follower.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",followers);
        return "/site/follower";
    }

    // userId的用户是否被 hostHolder用户关注
    private boolean hasFollowed(int userId){
        if(hostHolder.getUser()==null) return false;

        return followService.hasFollowed(hostHolder.getUser().getId(),CommunityConstant.ENTITY_TYPE_USER,userId);
    }
}














