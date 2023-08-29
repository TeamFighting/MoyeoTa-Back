package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthApiClient.OAuthApiClient;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
                // OAuthApiClient 객체의 oAuthProvider 값을 추출
                // Function.identity()는 각 객체 자체를 값으로 사용하도록 지정한 함수
        );
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}
