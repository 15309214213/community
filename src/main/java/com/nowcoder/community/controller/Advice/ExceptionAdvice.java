package com.nowcoder.community.controller.Advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {


    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletResponse response, HttpServletRequest request) throws IOException {

        logger.error("服务器发生异常", e.getMessage());

        for (StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }

        //处理异步请求，当请求头等于XMLHttpRequest为异步请求
        String str = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(str)){
            //为异步请求
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else {
            response.sendRedirect(request.getContextPath() + "/error");

        }


    }

}
