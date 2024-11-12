package com.example.educationplatform.controller;

import com.example.educationplatform.model.Message;
import com.example.educationplatform.model.User;
import com.example.educationplatform.service.IMessageService;
import com.example.educationplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/message")
public class MessageController {

    private final IMessageService messageService;
    private final IUserService userService;

    @Autowired
    public MessageController(IMessageService messageService, IUserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/showMessagePage/{receiverId}")
    public String getMessagePage(Model model, @AuthenticationPrincipal User user, @PathVariable Long receiverId){
        if(!Objects.equals(user.getId(), receiverId)) {
            List<Message> sentMessages = messageService.getMessageByUserAndReceiver(user, receiverId);
            User receiver = userService.getUserById(receiverId);
            List<Message> receivedMessages = messageService.getMessageByUserAndReceiver(receiver, user.getId());

            List<Message> messages = new ArrayList<>();
            messages.addAll(sentMessages);
            messages.addAll(receivedMessages);

            messages.sort(Comparator.comparing(Message::getMessageDate));

            Message message = new Message();
            message.setReceiverId(receiverId);
            model.addAttribute("messages", messages);
            model.addAttribute("message", message);
        } else {
            String message = "Ви не можете відправляти собі повідомлень";
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            return "redirect:/message/getChatPage?message=" + message;
        }
        return "message/message-page";
    }

    @PostMapping("/sendMessage")
    public String sendMessageToUser(@ModelAttribute Message message, @AuthenticationPrincipal User user){
        if(!Objects.equals(user.getId(), message.getReceiverId())) {
            message.setMessageDate(new Date());
            message.setUser(user);
            messageService.sendMessage(message);
        }
        return "redirect:/message/showMessagePage/" + message.getReceiverId();
    }

    @GetMapping("/getChatPage")
    public String getChatPage(@AuthenticationPrincipal User user, Model model){
        List<Long> receiversId= messageService.getUserChatsByReceiverId(user);
        List<Long> userReceiversIds = messageService.getDistinctUserIdByReceiverId(user.getId());
        List<Long> userChatIds = new ArrayList<>();
        userChatIds.addAll(receiversId);
        userChatIds.addAll(userReceiversIds);
        Set<Long> uniqueSet = new HashSet<>(userChatIds);
        userChatIds.clear();
        userChatIds.addAll(uniqueSet);

        List<User> users = new ArrayList<>();
        for(Long id : userChatIds) {
            users.add(userService.getUserById(id));
        }
        model.addAttribute("users", users);
        return "message/user-messages";
    }
}
