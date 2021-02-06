package com.anchris;

import com.anchris.dao.DiscussPostMapper;
import com.anchris.entity.DiscussPost;
import com.anchris.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Community01Application.class )
class Community01ApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Autowired
    DiscussPostMapper discussPostMapper;
    @Test
    void DiscussPost() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 20);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost.toString());
        }
        System.out.println(discussPostMapper.selectDiscussPostRows(149));
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }


    @Autowired
    UserService userService;
    @Test
    void testUer(){
        System.err.println(userService.findUserById(1).toString());
    }
}
