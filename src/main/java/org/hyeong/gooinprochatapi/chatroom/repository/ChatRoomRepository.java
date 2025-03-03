package org.hyeong.gooinprochatapi.chatroom.repository;

import org.hyeong.gooinprochatapi.chatroom.domain.ChatRoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoomEntity, String> {
}
