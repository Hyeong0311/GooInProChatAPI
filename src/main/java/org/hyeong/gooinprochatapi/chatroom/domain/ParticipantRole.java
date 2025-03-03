package org.hyeong.gooinprochatapi.chatroom.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ParticipantRole {

    ADMIN("Admin"),
    EMPLOYER("Employer"),
    PART_TIMER("partTimer");

    private final String role;

    ParticipantRole(String role) {

        this.role = role;
    }

    @JsonValue
    public String getRole() {

        return role;
    }
}
