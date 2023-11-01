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
    PARTICIPATION_DETAILS_ALREADY_CANCEL(400, "이미 참가 취소가 되었습니다.", 423),

    UNKNOWN_ERROR(500, "토큰이 존재하지 않습니다.", 1001),
    WRONG_TYPE_TOKEN(500, "변조된 토큰입니다.", 1002),
    EXPIRED_TOKEN(500, "만료된 토큰입니다.", 1003),
    UNSUPPORTED_TOKEN(500, "변조된 토큰입니다.", 1004),
    ACCESS_DENIED(500, "권한이 없습니다.", 1005),
    NO_INFO(500,"토큰에 해당하는 정보가 없습니다.", 1006);


    private final int status;
    private final String message;
    private final int code;

}
