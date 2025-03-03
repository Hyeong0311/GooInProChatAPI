package org.hyeong.gooinprochatapi.chatmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hyeong.gooinprochatapi.chatmessage.domain.ChatMessageEntity;
import org.hyeong.gooinprochatapi.chatmessage.dto.ChatMessageDTO;
import org.hyeong.gooinprochatapi.chatmessage.dto.ChatMessageReadDTO;
import org.hyeong.gooinprochatapi.chatmessage.repository.ChatMessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    public ChatMessageEntity saveMessageService(ChatMessageDTO chatMessageDTO) {

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .roomId(chatMessageDTO.getRoomId())
                .senderEmail(chatMessageDTO.getSenderEmail())
                .message(chatMessageDTO.getMessage())
                .sentAt(Date.from(Instant.now()))
                .build();

        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);

        log.info("Successfully saved Chat Message");

        return savedMessage;
    }

    //채팅방 들어갔을 때 메세지 가져오기
    public List<ChatMessageReadDTO> getMessageService(String roomId) {

        Criteria criteria = Criteria.where("roomId").is(roomId);

        Query query = new Query(criteria)
                .with(Sort.by(Sort.Order.desc("sentAt")));  // sentAt 기준 내림차순 정렬

        log.info("MongoDB Query: {}", query);

        return mongoTemplate.find(query, ChatMessageReadDTO.class, "chat_messages");
    }
}
