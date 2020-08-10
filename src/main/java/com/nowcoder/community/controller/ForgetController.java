package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPBinding;
import java.util.Map;

@Controller
public class ForgetController {
    @RequestMapping(path = "/site/forget",method = RequestMethod.GET)
    public String getForgerPage(){
        return "/site/forget";
    }

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping(path = "/forget",method = RequestMethod.POST)
    public String forget(Model model, User user){
        Map<String ,Object> map = userService.forget(user);
        if (map==null||map.isEmpty()){
            model.addAttribute("msg","重置成功");
            model.addAttribute("target","/site/login");
            return "/site/operate-result";

        }
        else {
            model.addAttribute("emailMsg",map.get("emailMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));

            return "/site/forget";
        }
    }

    @RequestMapping(path = "/forget",method = RequestMethod.GET)
    public void getSCode(HttpServletResponse response,HttpSession session){
        //生成验证码
        String sCode = kaptchaProducer.createText();

        //验证码存入
        session.setAttribute("sCode",sCode);


    }
}
