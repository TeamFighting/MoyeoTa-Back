package com.moyeota.moyeotaproject.component.OAuth.OAuthApiClient;

import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;

// 외부 API 요청 후 응답값을 리턴해주는 인터페이스
public interface OAuthApiClient {
    OAuthProvider oAuthProvider(); // client의 타입 반환

    String requestAccessToken(OAuthLoginParams params); // Authorization Code를 기반으로 인증 API를 요청해서 Access Token 휙득

    OAuthInfoResponse requestOauthInfo(String accessToken); // AccessToken을 기반 프로필 정보 휙득
}
