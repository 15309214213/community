package com.nowcoder.community.config;

import com.nowcoder.community.controller.interceptor.AlphaInterceptor;
import com.nowcoder.community.controller.interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//1
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
        .excludePathPatterns("/**/.css","/**/*.png","/**/.js","/**/.jpg","/**/.jpeg")//排除静态资源，让静态资源不被拦截
        .addPathPatterns("/register","/login");//拦截改路径,不写就是所有路径

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/.css","/**/*.png","/**/.js","/**/.jpg","/**/.jpeg");//排除静态资源，让静态资源不被拦截

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/**/.css","/**/*.png","/**/.js","/**/.jpg","/**/.jpeg");//排除静态资源，让静态资源不被拦截

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/.css","/**/*.png","/**/.js","/**/.jpg","/**/.jpeg");//排除静态资源，让静态资源不被拦截

    }



}
