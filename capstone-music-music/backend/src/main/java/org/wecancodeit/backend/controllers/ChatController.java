package org.wecancodeit.backend.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wecancodeit.backend.models.ChatMessage;
import org.wecancodeit.backend.repositories.ChatMessageRepository;

@Controller
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @MessageMapping("/api/message")
    public void sendMessage(ChatMessage chatMessage, Principal principal) {
        if (principal != null) {
            System.out.println("Message from principal: " + principal.getName());
        } else {
            System.out.println("Principal is null in message mapping");
        }
        chatMessage.setTimestamp(new Date());
        chatMessageRepository.save(chatMessage);
        
        String recipient = chatMessage.getRecipient();
        if (recipient != null) {
            messagingTemplate.convertAndSendToUser(recipient, "/queue/messages", chatMessage);
        } else {
            System.out.println("Recipient is null");
        }
    }

    @GetMapping("/api/message/history")
    @ResponseBody
    public List<ChatMessage> getMessageHistory(@RequestParam String user1, @RequestParam String user2) {
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSender(user1, user2, user1, user2);
    }
}
