package com.anchris;

import com.anchris.dao.MessageMapper;
import com.anchris.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Community01Application.class)
public class MessageTest {

    @Autowired
    MessageMapper messageMapper;
    @Test
    public void testMessageMapper(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 100);
        for (Message message : messages) {
            System.err.println(message.toString());
        }
        System.out.println("====================================================");
        System.out.println("====================================================");
        List<Message> messages1 = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message : messages1) {
            System.out.println(message.toString());
        }
        System.out.println("====================================================");
        System.out.println("====================================================");
        System.out.println(messageMapper.selectConversationCount(111));
        System.out.println("====================================================");
        System.out.println("====================================================");
        System.out.println(messageMapper.selectLetterUnreadCount(112, "111_112"));
        System.out.println(messageMapper.selectLetterUnreadCount(111, "111_112"));
    }

    @Test
    public void testSendMessage(){
        Message message = new Message();
        message.setId(355);
        message.setFromId(111);
        message.setToId(112);
        message.setContent("hello 112");
        message.setConversationId("111_112");
        message.setStatus(3);
        message.setCreateTime(new Date());
        messageMapper.insertMessage(message);
        System.out.println(message.getId());
    }

    @Test
    public void testUpdateMessageStatus(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(355);
        messageMapper.updateStatus(list,0);
    }
}
