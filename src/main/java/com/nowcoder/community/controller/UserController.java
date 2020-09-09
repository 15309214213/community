package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;



    @Autowired
    private HostHolder hostHolder;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    //--------------------
    @Value("${server.servlet.context-path}")
    private String contextPath;


    //--------------------
//    @Value("${qiniu.key.access}")
//    private String accessKey;
//
//    @Value("${qiniu.key.secret}")
//    private String secretKey;
//
//    @Value("${qiniu.bucket.header.name}")
//    private String headerBucketName;
//
//    @Value("${quniu.bucket.header.url}")
//    private String headerBucketUrl;

    //--------------------
//    @LoginRequired
//    @RequestMapping(path = "/setting", method = RequestMethod.GET)
//    public String getSettingPage(Model model) {
//        // 上传文件名称
//        String fileName = CommunityUtil.generateUUID();
//        // 设置响应信息
//        StringMap policy = new StringMap();
//        policy.put("returnBody", CommunityUtil.getJSONString(0));
//        // 生成上传凭证
//        Auth auth = Auth.create(accessKey, secretKey);
//        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);
//
//        model.addAttribute("uploadToken", uploadToken);
//        model.addAttribute("fileName", fileName);
//
//        return "/site/setting";
//    }
//---------------
//    // 更新头像路径
//    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
//    @ResponseBody
//    public String updateHeaderUrl(String fileName) {
//        if (StringUtils.isBlank(fileName)) {
//            return CommunityUtil.getJSONString(1, "文件名不能为空!");
//        }
//
//        String url = headerBucketUrl + "/" + fileName;
//        userService.updateHeader(hostHolder.getUser().getId(), url);
//
//        return CommunityUtil.getJSONString(0);
//    }

//原版代码
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    //第一个参数用来上传文件，第二个用来给页面返回数据
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage==null){
            model.addAttribute("error","请选择一张图片");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        //获取后缀以便改名
        String suffix = fileName.substring(fileName.lastIndexOf(".") );
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }


        //生成随机的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //设置文件的存放位置
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常",e);
        }
        //更新当前用户的头像的路径（web访问路径）
        //http://localhost:8081/community/user/header/xx.png
        User user = hostHolder.getUser();
        String headerUrl = domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);


        return "redirect:/index";
    }

    //原版代码
    //浏览器获取头像
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void gerHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务端存放的路径
        fileName = uploadPath+"/"+fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") +1 );
        response.setContentType("image/"+suffix);
        try(
                FileInputStream fileInputStream = new FileInputStream(fileName);
                OutputStream outputStream = response.getOutputStream();
                ){
            byte[] buffer = new byte[1024];
            int b=0;
            while((b = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0, b);
            }
        }catch (IOException e){
            logger.error("读取头像失败"+ e.getMessage());
        }
    }

    //修改密码
    @RequestMapping(path = "/changePassword",method = RequestMethod.POST)
    public String changePassword(String prePassword,String newPassword,Model model,@CookieValue("ticket") String ticket){

        if (prePassword==null||newPassword==null){
            model.addAttribute("error","请输入密码");
            return "/site/setting";
        }

        User user = hostHolder.getUser();
        Map<String,Object> map = userService.changePassword(user,prePassword,newPassword);
        if (map == null || map.isEmpty()){
            model.addAttribute("msg","修改成功");
            model.addAttribute("target","/index");
            userService.logout(ticket);
            return "/site/operate-result";
        }
        else {
            model.addAttribute("prePasswordMsg", map.get("prePasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            return "/site/setting";
        }

    }

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowerService followerService;
    //个人主页
    @RequestMapping(path = "/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model){

        User user = userService.findUserById(userId);
        if (user==null) {
            throw new RuntimeException("用户不存在");
        }

        //用户
        model.addAttribute("user", user);
        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        //关注数量
        long followeeCount = followerService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);

        //粉丝数量
        long followerCount = followerService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

        //是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser()!=null){
            hasFollowed = followerService.hasFollowde(hostHolder.getUser().getId(),ENTITY_TYPE_USER, userId);
        }

        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";

    }

    @Autowired
    private DiscussPostService discussPostService;
    //查看某人发布的帖子
    @RequestMapping(path = "/my-post/{userId}",method = RequestMethod.GET)
    public String getProfilePost(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if (user == null){
            return null;
        }
        model.addAttribute("user",user);

        page.setLimit(100);
        page.setPath("/my-post/" + userId);
        page.setRows(discussPostService.findDiscussPostRows(userId));

        List<DiscussPost> discussPostList = discussPostService.findDiscussPost(userId, page.getOffset(), page.getLimit(),0);
        List<Map<String,Object> > discussPosts = new ArrayList<>();
        if (discussPostList!=null){
            for (DiscussPost post : discussPostList){
                Map<String ,Object> map = new HashMap<>();
                map.put("post", post);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);

                discussPosts.add(map);

            }
        }

        model.addAttribute("discussPostCount",discussPostService.findDiscussPostRows(userId));
        model.addAttribute("discussPosts",discussPosts);
        //model.addAttribute("user",user);
        return "/site/my-post";

    }


    //查询某人的评论&回复
    @Autowired
    private CommentService commentService;
    @RequestMapping(path = "/my-reply/{userId}",method = RequestMethod.GET)
    public String getProfileComment(@PathVariable("userId") int userId, Model model, Page page){
        User user = userService.findUserById(userId);
        if (user == null){
            return null;
        }
        model.addAttribute("user",user);

        //page.setLimit(5);
        page.setPath("/my-reply/" + userId);
        page.setRows(commentService.findCommentCountByUserId(userId));

        List<Comment> commentList = commentService.findCommentsByUserId(userId, page.getOffset(), page.getLimit());
        List<Map<String,Object> > comments = new ArrayList<>();
        if (commentList!=null){
            for (Comment comment : commentList){
                if (comment==null){
                    break;
                }
                Map<String ,Object> map = new HashMap<>();
                map.put("comment", comment);

                int postId=comment.getEntityId();
                map.put("postId",postId);

                String postTitle = discussPostService.findDiscussPostById(postId).getTitle();
                map.put("postTitle",postTitle);

                comments.add(map);

            }
        }

        model.addAttribute("commentCount",commentService.findCommentCountByUserId(userId));
        model.addAttribute("comments",comments);
        //model.addAttribute("user",user);
        return "/site/my-reply";

    }


}


