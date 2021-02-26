package com.anchris.dao;

import com.anchris.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //当前用户的会话列表,返回每个会话的最新私信
    List<Message> selectConversations(int userId,int offset,int limit);

    //当前用户的会话数量
    int selectConversationCount(int userId);

    //查询当前会话的私信内容
    List<Message> selectLetters(String conversationId,int offset,int limit);

    //查询当前会话的私信数量
    int selectLetterCount(String conversationId);
    //查询当前未读 私信数量
    int selectLetterUnreadCount(int userId, String conversationId);

    //插入私信
    int insertMessage(Message message );
    // 改变会话状态
    int updateStatus(List<Integer>ids,int status);

    //某个 类型的最新通知
    Message selectLatestNotice(int userId,String topic);
    //某个类型通知的数量
    int selectNoticeCount(int userId,String topic);
    //某个类型通知的未读数量
    int selectNoticeUnreadCount(int userId,String topic);

    // 查询某个主题所包含的通知列表
    List<Message> selectNotices(int userId, String topic, int offset, int limit);



}
