package org.hyeong.gooinprochatapi.chatroom.dto;

import lombok.Data;

@Data
public class ChatRoomOutDTO {

    private String email;   //나갈 사람 email;

    private String roomId;  //나갈 채팅방 id
}
