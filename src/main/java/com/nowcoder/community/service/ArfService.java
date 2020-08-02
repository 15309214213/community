package com.nowcoder.community.service;

import com.nowcoder.community.dao.arfDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.PublicKey;

@Service
//@Scope("prototype")//生成多个实例，不写就只生成一个
public class ArfService {
    @Autowired
    private arfDao arfdao;
    public  ArfService() {
        System.out.println("实例化");
    }
    @PostConstruct
    public void init(){
        System.out.println("初始化");

    }
    @PreDestroy
    public void destory(){
        System.out.println("销毁");
    }
    public  String find(){
        return arfdao.select();
    }








}
