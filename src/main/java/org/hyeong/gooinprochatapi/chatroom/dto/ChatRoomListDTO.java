package org.hyeong.gooinprochatapi.chatroom.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "chat_rooms")
public class ChatRoomListDTO {

    private String id;

    private String roomName;    //채팅방 이름

    private String message; //마지막 메세지 내용

    private Date sentAt;   //마지막 메세지 시간
}
