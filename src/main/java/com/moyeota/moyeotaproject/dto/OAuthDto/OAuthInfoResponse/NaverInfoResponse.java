package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    static class Response {
        private String email;
        private String name;
        private String mobile;
        private String birthyear;
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
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("nowYear = " + nowYear);
        String birthyear = response.birthyear;
        System.out.println("birthyear = " + birthyear);
        try {
            int birthyearInt = Integer.parseInt(birthyear);
            System.out.println("birthyearInt = " + birthyearInt);
            int age = nowYear - birthyearInt;
            System.out.println("age = " + age);
            System.out.println("나이: " + age);
        } catch (NumberFormatException e) {
            System.out.println("생년월일이 올바른 형식이 아닙니다.");
        }
        return String.valueOf(response.age);
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
