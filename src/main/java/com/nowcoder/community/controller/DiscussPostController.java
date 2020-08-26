package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
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

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if (user==null){
            return CommunityUtil.getJSONString(403,"你还没有登陆");

        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        //以上步骤也有可能报错，以后统一处理
        return CommunityUtil.getJSONString(0,"发布成功");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    //当某一个实体和model同时作为函数参数时，springmvc会将该实体注入到model
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){


        //帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
        //作者查询
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);


        //帖子的回复和赞后续开发
        //查评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        /**
         * 评论：对于帖子的评论
         * 回复：对于评论的评论
         */
        //先获得对于帖子的评论，然后针对每个评论在获得其回复
        List<Comment> commentList = commentService.findCommentsByEntity(
                ENTITY_TYPE_POST,discussPost.getId(), page.getOffset(), page.getLimit());
        //每个map中封装一条（对于帖子的）评论
        List<Map<String ,Object>> commentVoList = new ArrayList<>();
        if (commentList != null){
            //开始给每个map中封装评论的数据
            for (Comment comment: commentList){
                Map<String ,Object> commentVo = new HashMap<>();
                commentVo.put("comment",comment);
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                //开始给每个评论装载其回复,每个list是一个评论的多个回复
                //获得回复
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String ,Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for (Comment reply : replyList){
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        //回复还需要装载 回复的目标（当回复是评论时没有target，当回复的是回复是需要target）
                        User target = reply.getTargetId() ==0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);

                        //装入该评论的每条回复
                        replyVoList.add(replyVo);
                    }
                }
                //装入该帖子的每条评论
                commentVo.put("replys", replyVoList);

                //回复的数量
                int replyVoCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyVoCount);


                commentVoList.add(commentVo);
            }
        }


        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }
}
