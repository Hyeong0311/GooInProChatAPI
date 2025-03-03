package org.hyeong.gooinprochatapi.chatmessage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hyeong.gooinprochatapi.chatmessage.domain.ChatMessageEntity;
import org.hyeong.gooinprochatapi.chatmessage.dto.ChatMessageDTO;
import org.hyeong.gooinprochatapi.chatmessage.dto.ChatMessageReadDTO;
import org.hyeong.gooinprochatapi.chatmessage.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/part/api/v1/chat")
@RequiredArgsConstructor
@Log4j2
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {

        log.info("Sending message: {}", chatMessageDTO);

        ChatMessageEntity chatMessageEntity = chatMessageService.saveMessageService(chatMessageDTO);

        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatMessageDTO.getRoomId(), chatMessageEntity);
    }

    @GetMapping("load/{roomId}")
    public ResponseEntity<List<ChatMessageReadDTO>> loadMessages(@PathVariable String roomId) {

        return ResponseEntity.ok(chatMessageService.getMessageService(roomId));
    }
}
