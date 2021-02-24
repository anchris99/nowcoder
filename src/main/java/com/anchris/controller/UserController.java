package com.anchris.controller;

import com.anchris.annotation.LoginRequired;
import com.anchris.entity.User;
import com.anchris.service.FollowService;
import com.anchris.service.LikeService;
import com.anchris.service.UserService;
import com.anchris.util.CommunityConstant;
import com.anchris.util.CommunityUtil;
import com.anchris.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    //跳转到  用户设置页面
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }


    //修改用户头像
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        //空值检查
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");

            return "/site/setting";
        }
        //上传文件的名字
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }
        // 生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    //获取 header image
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 设置响应格式为 图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }


    //修改密码
    @LoginRequired
    @RequestMapping(path = "/pwdUpdate",method = RequestMethod.POST)
    public String updatePwd(String oldPassword, String newPassword,String confirmPassword,Model model,@CookieValue("ticket") String ticket){
        Map<String, String> map = userService.updatePassword(hostHolder.getUser().getId(), oldPassword, newPassword, confirmPassword, hostHolder.getUser().getSalt());
        if(map==null||map.isEmpty()) {
            userService.logout(ticket);
            return "/site/login";
        }else{
            model.addAttribute("oldPwdMsg",map.get("oldPwdMsg"));
            model.addAttribute("newPwdMsg",map.get("newPwdMsg"));
            model.addAttribute("confirmPwdMsg",map.get("confirmPwdMsg"));
            return "/site/setting";
        }
    }

    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfile(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user==null)
            throw new RuntimeException("用户不存在!");
        model.addAttribute("user",user);
        int userLikeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("userLikeCount",userLikeCount);


        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }
}
