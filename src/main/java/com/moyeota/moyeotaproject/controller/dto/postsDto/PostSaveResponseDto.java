package com.moyeota.moyeotaproject.controller.dto.postsDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "모집글 저장 응답")
@Getter
public class PostSaveResponseDto {
    @ApiModelProperty(value = "모집글 인덱스")
    private Long postId;
    @ApiModelProperty(value = "채팅방 인덱스")
    private Long chatRoomId;

    @Builder
    public PostSaveResponseDto(Long postId, Long chatRoomId) {
        this.postId = postId;
        this.chatRoomId = chatRoomId;
    }
}
