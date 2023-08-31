package com.moyeota.moyeotaproject.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "모집글 수정 요청")
public class PostsUpdateRequestDto {

    @ApiModelProperty(value = "모집글 제목", example = "서울에서 제주까지 가즈아~~", required = true)
    private String title;

    @ApiModelProperty(value = "내용", example = "제주도에 같이 갈 사람은 참가 신청 ㄱㄱ", required = true)
    private String content;

    @Builder
    public PostsUpdateRequestDto(String title, String content){
        this.title = title;
        this.content = content;
    }

}