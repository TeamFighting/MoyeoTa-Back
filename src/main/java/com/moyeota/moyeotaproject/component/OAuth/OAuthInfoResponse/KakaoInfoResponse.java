package com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
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
        private String age_range;
        private String gender;
    }

    @Getter
    static class KakaoProfile {
        private String profile_image_url;
        private String nickname;
    }

    public String getAge(){
        return kakaoAccount.getAge_range();
    }

    public String getGender(){
        return kakaoAccount.getGender();
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getUsername() {
        return kakaoAccount.name;
    }

    @Override
    public String getProfileImage() {
        return kakaoAccount.profile.getProfile_image_url();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
