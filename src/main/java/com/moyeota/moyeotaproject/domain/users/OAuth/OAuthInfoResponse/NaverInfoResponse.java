package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.Getter;

@Getter
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    static class Response {
        private String email;
        private String name;
        private String mobile; // 카카오에는 없어서.. 어떻게 처리할지 미정..
        private String profile_image;
        private String gender;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getUsername() {
        return response.getName();
    }

    @Override
    public String getProfileImage() {
        return response.getProfile_image();
    }

    @Override
    public String getGender() {
        return response.getGender();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
