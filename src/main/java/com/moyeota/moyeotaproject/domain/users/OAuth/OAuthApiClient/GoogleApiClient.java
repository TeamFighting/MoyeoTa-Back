package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthApiClient;


import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient{

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.url.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        return null;
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        return null;
    }
}
