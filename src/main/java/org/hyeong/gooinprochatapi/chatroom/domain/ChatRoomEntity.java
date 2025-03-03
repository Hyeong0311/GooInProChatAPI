package org.hyeong.gooinprochatapi.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoomEntity {

    @Id
    private String id;

    private String roomName;    //채팅방 이름

    private Date roomCreatedAt;    //채팅방 생성 시간

    private Date roomUpdatedAt;    //채팅방 update 시간

    private String createdBy;   //채팅방 생성자 이메일

    private List<Participant> participants; //참여자 배열
}