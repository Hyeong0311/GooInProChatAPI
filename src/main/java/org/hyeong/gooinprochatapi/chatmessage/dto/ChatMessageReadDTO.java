package org.hyeong.gooinprochatapi.chatmessage.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageReadDTO {

    private String roomId;      // 채팅방 ID

    private String senderEmail; // 보낸 사람 이메일

    private String message;     // 채팅 메시지

    private Date sentAt;        // 보낸 시간
}
