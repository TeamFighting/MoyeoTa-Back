package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile profile;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private String email;
        private String birthyear;
        private String name; // 카카오계정의 이름
        private Boolean is_email_verified; // 이메일 인증 여부
        private String age_range;
        private String gender;
        private String phone_number;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String profile_image_url;
        private String nickname;
    }

    public String getAge() {
        return kakaoAccount.age_range;
    }

    public String getGender() {
        return kakaoAccount.gender;
    }

    @Override
    public String getPhoneNumber() {
        return kakaoAccount.getPhone_number();
    }

    @Override
    public String getEmail() {
        System.out.println("동의 : " + kakaoAccount.email_needs_agreement);
        System.out.println("동의 : " + kakaoAccount.is_email_verified);
        System.out.println("동의 : " + kakaoAccount.is_email_valid);
        return kakaoAccount.email;
    }

    @Override
    public String getUsername() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getProfileImage() {
        return kakaoAccount.profile.profile_image_url;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
