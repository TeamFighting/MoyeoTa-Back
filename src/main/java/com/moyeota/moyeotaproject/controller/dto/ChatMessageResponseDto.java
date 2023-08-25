package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private String message;
    private String sender;

    @Builder
    public ChatMessageResponseDto(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
}
