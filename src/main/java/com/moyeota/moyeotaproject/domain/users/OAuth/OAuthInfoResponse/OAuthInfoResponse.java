package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;

// Access Token 으로 요청한 외부 API 프로필 응답값을 Model로 변환시키기 위한 인터페이스
public interface OAuthInfoResponse {
    String getUsername();
    String getEmail();
    String getProfileImage();
    String getAge();
    String getGender();
    String getPhoneNumber();
    OAuthProvider getOAuthProvider();
}
