package org.hyeong.gooinprochatapi.chatroom.dto;

import lombok.Data;
import org.hyeong.gooinprochatapi.chatroom.domain.Participant;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "chat_rooms")
public class ChatRoomAddDTO {

    private String roomName;    //채팅방 이름

    private String createdBy;   //채팅방 생성자 이메일

    private List<Participant> participants; //참여자 배열
}
