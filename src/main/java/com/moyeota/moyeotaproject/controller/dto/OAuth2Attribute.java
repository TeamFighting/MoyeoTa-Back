package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class OAuth2Attribute {

    private String provider; // 소셜로그인 종류
    private String userId;
    private String username;
    private String email;
    private String picture;
    private String nickname;

    public static OAuth2Attribute of(String provider, String usernameAttributeName, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return OAuth2Attribute.ofGoogle(provider, usernameAttributeName, attributes);
            case "kakao":
                return OAuth2Attribute.ofKakao(provider, usernameAttributeName, attributes);
            case "naver":
                return OAuth2Attribute.ofNaver(provider, usernameAttributeName, attributes);
            default:
                throw new RuntimeException("소셜 로그인 접근 실패");
        }

    }

    private static OAuth2Attribute ofGoogle(String provider, String usernameAttributeName, Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .provider(provider)
                .username(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .userId(String.valueOf(attributes.get(usernameAttributeName)).concat("google"))
                .build();
    }

    private static OAuth2Attribute ofKakao(String provider, String usernameAttributeName, Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .provider(provider)
                .username(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .userId(String.valueOf(attributes.get(usernameAttributeName)).concat("kakao"))
                .build();
    }

    private static OAuth2Attribute ofNaver(String provider, String usernameAttributeName, Map<String, Object> attributes) {

        return OAuth2Attribute.builder()
                .provider(provider)
                .username(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .userId(String.valueOf(attributes.get(usernameAttributeName)).concat("naver"))
                .build();
    }
}
