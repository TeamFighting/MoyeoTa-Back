package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.Getter;

@Getter
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    static class KakaoAccount {
        private KakaoProfile profile;
        private String name;
        private String email;
        private String gender;
    }

    @Getter
    static class KakaoProfile {
        private String profile_image_url;
        private String nickname;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public String getUsername() {
        return kakaoAccount.profile.getNickname();
    }

    @Override
    public String getProfileImage() {
        return kakaoAccount.profile.getProfile_image_url();
    }

    @Override
    public String getGender() {
        return kakaoAccount.getGender();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
