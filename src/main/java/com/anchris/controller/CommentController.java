package com.anchris.controller;

import com.anchris.entity.Comment;
import com.anchris.service.CommentService;
import com.anchris.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(value = "/add/{discussPostId}" ,method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setCreateTime(new Date());
        comment.setStatus(0);
        commentService.addComment(comment);

        return "redirect:/discuss/detail/"+discussPostId;

    }

}
