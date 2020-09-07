package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //授权
        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "unfollow"
                )
                .hasAnyAuthority(
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR,
                        AUTHORITY_USER
                )
                .antMatchers("/discuss/top",
                        "/discuss/wonderful")
                .hasAnyAuthority(AUTHORITY_MODERATOR)

                .antMatchers("/discuss/delete",
                        "/data/**")
                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll()
                .and().csrf().disable();

        //权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    //没有登陆
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //如果异步请求(判断消息头的值)
                            String xRequestedWith = request.getHeader("x-requested-with");
                            if ("XMLHttpRequest".equals(xRequestedWith)){
                                response.setContentType("application/plain;charset=utf-8");
                                PrintWriter writer = response.getWriter();
                                writer.write(CommunityUtil.getJSONString(403, "你还没有登陆"));
                            }
                        //如果正常请求
                            else{
                                response.sendRedirect(request.getContextPath() + "/login");
                            }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    //权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        //如果异步请求(判断消息头的值)
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403, "你还没有权限"));
                        }
                        //如果正常请求
                        else{
                            response.sendRedirect(request.getContextPath() + "/denied");
                        }
                    }
                });

        //因为security会默认拦截logout，因此需要覆盖他拦截，执行自己的
        http.logout().logoutUrl("/securitylogout");//欺骗其拦截

    }
}
