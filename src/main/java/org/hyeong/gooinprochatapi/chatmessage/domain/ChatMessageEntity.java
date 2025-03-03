package org.hyeong.gooinprochatapi.chatmessage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessageEntity {

    @Id
    private String id;

    private String roomId;  //채팅방 id

    private String senderEmail;  //채팅 보낸 user email;

    private String message; //채팅 message

    private Date sentAt;   //채팅 보낸 시간
}
