package org.hyeong.gooinprochatapi.chatmessage.repository;

import org.hyeong.gooinprochatapi.chatmessage.domain.ChatMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {

    void deleteAllByRoomId(String roomId);
}
