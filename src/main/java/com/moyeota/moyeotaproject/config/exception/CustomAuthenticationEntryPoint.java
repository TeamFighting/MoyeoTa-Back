package com.moyeota.moyeotaproject.config.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		Integer exception = (Integer)request.getAttribute("exception");

		if (exception == null) {
			setResponse(response, ErrorCode.UNKNOWN_ERROR);
		} else if (exception == 1004) { //잘못된 타입의 토큰인 경우
			setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
		} else if (exception == 1005) { //토큰 만료된 경우
			setResponse(response, ErrorCode.EXPIRED_TOKEN);
		} else if (exception == 1006) { //지원되지 않는 토큰인 경우
			setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
		} else {
			setResponse(response, ErrorCode.ACCESS_DENIED);
		}
	}

	private void setResponse(HttpServletResponse response, ErrorCode exceptionCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		JSONObject responseJson = new JSONObject();
		responseJson.put("message", exceptionCode.getMessage());
		responseJson.put("code", exceptionCode.getCode());

		response.getWriter().print(responseJson);
	}
}
