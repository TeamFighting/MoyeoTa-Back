package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Getter
@NoArgsConstructor
public class GoogleLoginParams implements OAuthLoginParams {

    private String authorizationCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", decodeUrl(authorizationCode));
        return body;
    }

    private String decodeUrl(String encodedUrl) {
        if (encodedUrl == null) {
            throw new IllegalArgumentException("Encoded URL은 null이 될 수 없습니다.");
        }
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }
}
