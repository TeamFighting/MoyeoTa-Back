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
        private String mobile;
        private String profile_image;
        private String age;
        private String gender; // F: 여자 M: 남자 U: 확인불가
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
    public String getAge(){
        return response.getAge();
    }

    @Override
    public String getGender() {
        return response.getGender();
    }

    @Override
    public String getPhoneNumber() {
        return response.getMobile();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
