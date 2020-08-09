package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import com.nowcoder.community.config.KaptchaConfig;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("$server.servlet.context-path")
    private String contextPath;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginController.class);
    //private static final Logger logger = (Logger) LoggerFactory.getLogger(LoginController.class);


    //访问注册页面
    @RequestMapping(path= "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }
    //访问登录页面
    //denglu页面相当于一个查询是否激活的页面，因此使用get
    @RequestMapping(path= "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    //访问忘记密码页面
    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage(){
        return "/site/forget";
    }


    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    //http://localhost:8081/community/avtivation/101/code
    //激活页面相当于一个查询是否激活的页面，因此使用get
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code){
        int result = userService.activation(userId,code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功!");
            model.addAttribute("target", "/login");
        }
        else if (result == ACTIVATION_REPEAT){
            model.addAttribute("msg", "操作无效，不要重复激活账号!");
            model.addAttribute("target", "/index");
        }
        else{
            model.addAttribute("msg", "注册失败，激活码不正确!");
            model.addAttribute("target", "/login");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    //由于是敏感数据，且不能存在浏览器短
    public void getKaptcha(HttpServletResponse httpServletResponse , HttpSession httpSession){
        //生成验证码
        //由于是敏感数据，且不能存在浏览器cun
        //生成一个随即祖父穿
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        httpSession.setAttribute("kaptcha",text);

        //把图片输出给浏览器
        httpServletResponse.setContentType("image/png");
        try {
            OutputStream outputStream = httpServletResponse.getOutputStream();
            //spring mvc会自动关闭该流
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("服务器响应错误" + e.getMessage());
        }
    }


    //登录，此相应路径虽然和上边的一个方法路径相同，但是请求方式不同，因此不冲突
    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememberme,
                        Model model,HttpSession session, HttpServletResponse response){

        //判断验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)
        ||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "site/login";
        }


        //检查账号密码
        int expiredSeconds = rememberme?REMEBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map = userService.login(username,password,expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie =new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);

            //此处重定向：因为登录请求已经完成了，请求已经关闭，只是需要跳转到推荐的页面
            return "redirect:/index";
        }
        else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);

        //重定向默认是get请求
        return "redirect:/login";
    }

}

