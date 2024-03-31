package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthApiClient;

import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;

public interface OAuthApiClient {
    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginParams params);

    OAuthInfoResponse requestOauthInfo(String accessToken);
}
