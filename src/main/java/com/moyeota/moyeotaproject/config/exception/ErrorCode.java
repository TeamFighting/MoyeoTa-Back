package com.moyeota.moyeotaproject.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    POSTS_EMPTY_TITLE(400, "제목을 입력하세요.", 417),
    POSTS_EMPTY_DEPARTURE(400, "출발지를 입력하세요", 418),
    POSTS_EMPTY_DESTINATION(400, "목적지를 입력하세요", 419),
    POSTS_EMPTY_DEPARTURE_TIME(400, "출발시각을 입력하세요", 420),
    POSTS_ALREADY_FINISH(400, "이미 마감된 공고입니다.", 421),

    PARTICIPATION_DETAILS_ALREADY_JOIN(400, "이미 참가 신청이 되었습니다.", 422),
    PARTICIPATION_DETAILS_ALREADY_CANCEL(400, "이미 참가 취소가 되었습니다.", 423);



    private final int status;
    private final String message;
    private final int code;

}
