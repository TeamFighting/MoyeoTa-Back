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

	TOTAL_DETAIL_EMPTY_DISTANCE(400, "최종 거리를 입력하세요.", 424),
	TOTAL_DETAIL_EMPTY_PAYMENT(400, "최종 금액을 입력하세요.", 425),

	INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", 1111),
	UNKNOWN_ERROR(401, "토큰이 존재하지 않습니다.", 1001),
	WRONG_TYPE_TOKEN(401, "변조된 토큰입니다.", 1002),
	EXPIRED_TOKEN(401, "만료된 토큰입니다.", 1003),
	UNSUPPORTED_TOKEN(401, "변조된 토큰입니다.", 1004),
	ACCESS_DENIED(401, "권한이 없습니다.", 1005),
	NO_INFO(401, "토큰에 해당하는 정보가 없습니다.", 1006),

	NO_EMAIL_ERROR(422, "소셜 로그인으로부터 이메일을 받아올 수 없습니다", 1007),
	MAIL_SEND_ERROR(400, "학교 인증 메일 전송에 실패하였습니다."),

	INVALID_USER(400, "해당하는 사용자가 없습니다"),

	INPUT_ERROR(400, "입력값에 오류가 있습니다."),
	INVALID_SCHOOL_EMAIL_CODE(400, "학교 인증 코드가 정확하지 않습니다."), INVALID_EMAIL(400,"해당하는 이메일이 존재하지 않습니다." );

	private final int status;
	private final String message;
	private final int code;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
		this.code = status;
	}
}
