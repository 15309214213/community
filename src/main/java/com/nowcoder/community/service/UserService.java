package com.nowcoder.community.service;

import com.nowcoder.community.controller.LoginTicket;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    public User findUserById(int id){
        //return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null){
            user = initCache(id);
        }
        return user;
    }

    //用户注册
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;


    @Value("${community.path.domain}")
    //community.path.domain=http://localhost:8080
    private String domain;


    @Value("${server.servlet.context-path}")
    //
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;


    /*@Autowired
    private LoginTicketMapper loginTicketMapper;*/



    //注册返回的数据各式各样，因此用map返回
    public Map<String ,Object> register(User user){
        //string值于输入框的名称相同 object存放的时应该显示 的信息
        Map<String,Object> map = new HashMap<>();

        //IF null
        if(user == null){
            throw new IllegalArgumentException("cant null");
        }
        if(StringUtils.isAllBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;

        }
        if(StringUtils.isAllBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;

        }
        if(StringUtils.isAllBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;

        }
        //验证账号
        User u=userMapper.selectByName(user.getUsername());
        if (u!=null){
            map.put("usernameMsg","账号已存在");
            return map;
        }
        //验证邮箱
        u=userMapper.selectByName(user.getEmail());
        if (u!=null){
            map.put("emailMsg","邮箱已存在");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        //set 头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //发邮件
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        //发出的邮件有一个激活链接 形式如下
        //http://localhost:8081/community/avtivation/101/code
        String url=domain + contextPath + "/activation/" + user.getId() + "/" +
                user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活",content);

         return  map;
    }

    //点击邮件激活：
    public  int activation(int userId,String code){
        User user=userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }

    }

    //忘记密码
    public Map<String ,Object> forget(User user){
        Map<String ,Object> map = new HashMap<>();
        //handle null
        if (user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
        }


        //验证邮箱
        User u = userMapper.selectByEmail(user.getEmail());
        if(u==null){
            map.put("emailMsg","邮箱不存在");
            return map;
        }
        String sCode = CommunityUtil.generateUUID().substring(0,5);

        //邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String content  = templateEngine.process("/mail/forget",context);
        mailClient.sendMail(user.getEmail(),"忘记密码",content);
        return map;
    }


    //登录（返回会有多种情况，因此返回map）
    public Map<String ,Object> login(String username, String password, long expriedSeconds){
        Map<String ,Object> map = new HashMap<>();

        //null值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能空");
            return map;
        }

        //验证账号
        User user= userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","账户不存在");
            return map;
        }
        //验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","账户未激活");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("usernameMsg","密码不正确");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket= new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expriedSeconds*1000));
        /*loginTicketMapper.insertLoginTicket(loginTicket);*/
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket);


        map.put("ticket", loginTicket.getTicket());

        return map;
    }


    public void logout(String ticket){
        /*loginTicketMapper.updateStatus(ticket,1);*/
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey,loginTicket);
    }


    //查询凭证
    public LoginTicket  findLoginTicket(String ticket){
        /*return loginTicketMapper.selectByTicket(ticket);*/
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    //更新头像路径
    public int updateHeader(int userId,String headerUrl){
        int rows = userMapper.updateHeader(userId,headerUrl);
        clearCache(userId);
        return rows;
    }


    //修改密码
    public Map<String ,Object> changePassword(User user, String prePassword , String newPassword){


        Map<String ,Object> map = new HashMap<>();
        //原验证密码
        if (StringUtils.isBlank(newPassword)){
            map.put("prePasswordMsg","原密码不能空");
            return map;
        }
        prePassword = CommunityUtil.md5(prePassword + user.getSalt());
        if (!user.getPassword().equals(prePassword)){
            map.put("prePasswordMsg","原密码不正确");
            return map;
        }
        if (StringUtils.isBlank(newPassword)){
            map.put("newPasswordMsg","新密码不能空");
            return map;
        }
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        clearCache(user.getId());
        userMapper.updatePassword(user.getId(), newPassword);
        return map;
    }

    //根据名称查user
    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }

    //1.在登录时先尝试从缓存中取用户信息
    private User getCache(int userId){
        String  redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }


    //2.如果取不到，就先从数据库取存入缓存，初始化缓存
    private User initCache(int userId){
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //3.当用户信息被修改,直接删除缓存中的数据重新载入。
    private void clearCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
