package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Getter
@NoArgsConstructor
public class GoogleLoginParams implements OAuthLoginParams{

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

    public String decodeUrl(String encodedUrl) {
        String decodedUrl = null;
        try {
            decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return decodedUrl;
    }
}
