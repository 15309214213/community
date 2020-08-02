package com.nowcoder.community.controller;

import com.nowcoder.community.service.ArfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private ArfService arfService;
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHELLO(){
        return  "hello spring boot.";
    }
    @RequestMapping("/data")
    @ResponseBody
    public  String getData(){
        return arfService.find();
    }

    @RequestMapping("/http")
    public  void http(HttpServletRequest request, HttpServletResponse response){
        //request 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration <String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));
        //response 给浏览器返回响应数据
        response.setContentType("text/heml;charset=utf-8");
        //respon 封装了输出流，向浏览器输出即可
        try {
            PrintWriter writer= response.getWriter();
            writer.write("nowcoder");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    //GET请求


    // /students?current = 1&limit = 20查询
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public  String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return  "some stu";
    }
    // /student/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a stu";
    }

    //POST请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){

        System.out.println(name);
        System.out.println(age);
        return "ok";

    }
    //响应动态html数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","zhangsan");
        mav.addObject("age","12");
        mav.setViewName("/demo/view");
        return mav;

    }
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getschool(Model model){
        model.addAttribute("name","xdu");
        model.addAttribute("age","80");
        return "/demo/view";
    }
    //响应json（异步请求:当前网页不刷新，得到新数据）
    //java对象 浏览器用js对象解析java对象（java-》josn-》js）
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String ,Object> getEmp(){
         Map<String ,Object> emp=new HashMap<>();
         emp.put("name","zhangsan");
         emp.put("age",23);
         emp.put("salary",1000.001);
         return emp;
    }
    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String ,Object> > getEmps(){
        List<Map<String ,Object> > list=new ArrayList<>();
        Map<String ,Object> emp=new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        emp.put("salary",1000.001);
        list.add(emp);
        emp=new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        emp.put("salary",1000.001);
        list.add(emp);

        emp=new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        emp.put("salary",1000.001);
        list.add(emp);
        emp=new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        emp.put("salary",1000.001);
        list.add(emp);
        emp=new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        emp.put("salary",1000.001);
        list.add(emp);
        return list;
    }






}
