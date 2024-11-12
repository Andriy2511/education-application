package com.example.educationplatform.service.implementation;

import com.example.educationplatform.model.Message;
import com.example.educationplatform.model.User;
import com.example.educationplatform.repository.MessageRepository;
import com.example.educationplatform.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getMessageByUserAndReceiver(User sender, Long receiverId){
        return messageRepository.findByUserAndReceiverId(sender, receiverId);
    }

    @Override
    public Message sendMessage(Message message){
        return messageRepository.save(message);
    }

    @Override
    public List<Long> getUserChatsByReceiverId(User user){
        return messageRepository.getUserChatsByReceiverId(user);
    }

    @Override
    public List<Long> getDistinctUserIdByReceiverId(Long receiverId){
        return messageRepository.getDistinctUserIdByReceiverId(receiverId);
    }
}
