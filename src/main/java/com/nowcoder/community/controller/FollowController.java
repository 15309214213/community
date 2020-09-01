package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowerService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
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
public class FollowController implements CommunityConstant {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private HostHolder hostHolder;

    //关注，异步
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId){
        //可以先使用拦截器判断是否登录
        User user = hostHolder.getUser();

        followerService.follow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0,"已关注");


    }

    //取消关注，异步
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        //可以先使用拦截器判断是否登录
        User user = hostHolder.getUser();

        followerService.unfollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0,"已取消关注");
    }

    @Autowired
    private UserService userService;
    ///查询某个用户关注的人
    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user",user);

        page.setLimit(6);
        page.setPath("/followees/" + userId);
        page.setRows((int) followerService.findFolloweeCount(userId,CommunityConstant.ENTITY_TYPE_USER));

        List<Map<String , Object>> userList = followerService.findFollowees(userId,page.getOffset(),page.getLimit());
        if (userList!=null){
            for (Map<String, Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/followee";
    }

    ///查询某个用户的粉丝
    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user",user);

        page.setLimit(6);
        page.setPath("/followers/" + userId);
        page.setRows((int) followerService.findFollowerCount(ENTITY_TYPE_USER,userId));

        List<Map<String , Object>> userList = followerService.findFollowers(userId,page.getOffset(),page.getLimit());
        if (userList!=null){
            for (Map<String, Object> map : userList){
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId){
        if (hostHolder.getUser()==null){
            return false;
        }
        return followerService.hasFollowde(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
    }


}
