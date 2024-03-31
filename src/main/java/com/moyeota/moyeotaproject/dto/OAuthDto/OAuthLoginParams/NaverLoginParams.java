package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class NaverLoginParams implements OAuthLoginParams {

    private String authorizationCode;
    private static final String state = "hLiDdL2uhPtsftcU";

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        if (authorizationCode.contains("&state")) {
            authorizationCode = authorizationCode.substring(0, authorizationCode.indexOf("&state"));
        }
        body.add("code", authorizationCode);
        body.add("state", state);
        return body;
    }
}
