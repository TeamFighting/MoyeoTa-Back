package com.moyeota.moyeotaproject.component.OAuth.OAuthApiClient;


import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.component.OAuth.OAuthTokens.GoogleTokens;
import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.GoogleInfoResponse;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    private static final String REDIRECT_URI = "https://moyeota-webview.netlify.app"; // 이후에 앱 등록 후 변경

    @Value("${oauth.google.url.api}")
    private String apiUrl;

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = apiUrl + "/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = params.makeBody();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("grant_type", GRANT_TYPE);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        GoogleTokens response = restTemplate.postForObject(url, request, GoogleTokens.class);

        return response.getIdToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        System.out.println("accessToken = " + accessToken);
        String url = apiUrl + "/tokeninfo?id_token=" + accessToken;

        return restTemplate.getForObject(url, GoogleInfoResponse.class);
    }
}
