package org.wecancodeit.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wecancodeit.backend.models.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSender(String sender, String recipient, String recipient2,
            String sender2);
}

