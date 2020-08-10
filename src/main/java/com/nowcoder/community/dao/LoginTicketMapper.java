package com.nowcoder.community.dao;

import com.nowcoder.community.controller.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {
    //这些方法不仅可以在xml文件中配置，也可以采用如下方法



    //insert
    @Insert({
            "insert into login_ticket(user_id , ticket , status , expired )" ,
            "value(#{userId}, #{ticket}, #{status}, #{expired} )"
    })
    @Options(useGeneratedKeys = true , keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    //select
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    //update 凭证数据失效
    @Update({
            "<script>",
            "update login_ticket set status = #{status} where ticket = #{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);
}
