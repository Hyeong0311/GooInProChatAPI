package org.hyeong.gooinprochatapi.chatroom.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat_rooms")
//1대 1 채팅 찾기 DTO
public class ChatRoomFindDTO {

    private String senderEmail;    //보내는 사람 Email

    private String recipientEmail;  //받는 사람 Email
}
