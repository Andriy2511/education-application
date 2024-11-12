package com.example.educationplatform.service;

import com.example.educationplatform.model.Message;
import com.example.educationplatform.model.User;

import java.util.List;

public interface IMessageService {
    List<Message> getMessageByUserAndReceiver(User sender, Long receiverId);

    Message sendMessage(Message message);

    List<Long> getUserChatsByReceiverId(User user);

    List<Long> getDistinctUserIdByReceiverId(Long receiverId);
}
