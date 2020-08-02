package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("zidingyi")
//可以自定义名字
public class arfDaoHiber implements arfDao {
    @Override
    public String select(){
        return "hiber";
    }
}
