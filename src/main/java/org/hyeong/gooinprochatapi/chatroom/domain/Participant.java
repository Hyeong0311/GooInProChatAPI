package org.hyeong.gooinprochatapi.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Participant {

    private String email;   //email

//    private ParticipantRole role;   //역할(Admin, Employer, PartTimer)

    private Date joinedAt;   //참여 시간

    private Date leftAt;    //퇴장 시간(null 이면 아직 퇴장 X)

    public Participant(String email, Date joinedAt) {

        this.email = email;
        this.joinedAt = joinedAt;
    }
}
