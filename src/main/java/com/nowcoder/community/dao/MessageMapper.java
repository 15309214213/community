package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {


    //查询当前用户的所有会话列表，针对每个会话，返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询一个会话的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //查新某个会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读的私信数量
    int selectLetterUnreadCount(int userId, String conversationId);

    //新增消息
    int insertMessage(Message message);

    //修改消息状态
    int updateStatus(List<Integer> ids, int status);

    //查找某条私信
    Message findMessageById(int id);

    //删除私信详情
    int deleteLetter(int id);

    //查询某个主题下的最新通知
    Message selectLatestNotice(int userId, String topic);

    //查询某个主题包含的通知数量
    int selectNoticeCount(int userId, String topic);

    //查询未读通知数量
    int selectNoticeUnreadCount(int userId, String topic);

    //查询某个主题包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);

}
