package com.nowcoder.community.dao;


import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
   /* //mysql 使用分页会使用limit方法，有两个参数
    List<DiscussPost>  selectDiscussPosts(int userId,int offset,int limit);//offset是每页起始行号，limit为每页个数

    //查询有多少数据
    //@param用于给参数取别名
    //如果需要动态得得到一个条件，并且方法只有一个参数，就需要给参数取别名
    int selectDiscussPostRows( @Param("userId")  int userId);*/


   List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);


    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);


}
