package com.moyeota.moyeotaproject.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moyeota.moyeotaproject.component.OAuth.OAuthApiClient.OAuthApiClient;
import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestOAuthInfoService {
	private final Map<OAuthProvider, OAuthApiClient> clients;

	public RequestOAuthInfoService(List<OAuthApiClient> clients) {
		this.clients = clients.stream().collect(
			Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
		);
	}

	public OAuthInfoResponse request(OAuthLoginParams params) {
		OAuthApiClient client = clients.get(params.oAuthProvider());
		String accessToken = client.requestAccessToken(params);
		return client.requestOauthInfo(accessToken);
	}
}
