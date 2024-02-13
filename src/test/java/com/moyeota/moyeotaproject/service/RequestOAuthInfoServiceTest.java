package com.moyeota.moyeotaproject.service;

import static org.junit.jupiter.api.Assertions.*;

import com.moyeota.moyeotaproject.component.OAuth.OAuthApiClient.OAuthApiClient;
import com.moyeota.moyeotaproject.component.OAuth.OAuthInfoResponse.OAuthInfoResponse;
import com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams.OAuthLoginParams;
import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class RequestOAuthInfoServiceTest {

    public class DefaultOAuthInfoResponse implements OAuthInfoResponse {
        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public String getEmail() {
            return null;
        }

        @Override
        public String getProfileImage() {
            return null;
        }

        @Override
        public String getAge() {
            return null;
        }

        @Override
        public String getGender() {
            return null;
        }

        @Override
        public String getPhoneNumber() {
            return null;
        }

        @Override
        public OAuthProvider getOAuthProvider() {
            return null;
        }
    }

    public class DefaultOAuthLoginParams implements OAuthLoginParams {

        @Override
        public OAuthProvider oAuthProvider() {
            return null;
        }

        @Override
        public MultiValueMap<String, String> makeBody() {
            return null;
        }
    }


//    @Test
//    void testRequestOAuthInfo() {
//        // Mock OAuthApiClient instances
//        OAuthApiClient googleApiClient = Mockito.mock(OAuthApiClient.class);
//        OAuthApiClient kakaoApiClient = Mockito.mock(OAuthApiClient.class);
//        OAuthApiClient naverApiClient = Mockito.mock(OAuthApiClient.class);
//
//        OAuthInfoResponse googleResponse = new DefaultOAuthInfoResponse();
//        OAuthInfoResponse kakaoResponse = new DefaultOAuthInfoResponse();
//        OAuthInfoResponse naverResponse = new DefaultOAuthInfoResponse();
//
//        // Mock OAuthLoginParams instances
//        // OAuthLoginParams googleLoginParams = new DefaultOAuthLoginParams(OAuthProvider.GOOGLE);
//        // OAuthLoginParams kakaoLoginParams = new DefaultOAuthLoginParams(OAuthProvider.KAKAO);
//
//        // Create a map of OAuthProvider to OAuthApiClient
//        Map<OAuthProvider, OAuthApiClient> clientsMap = Map.of(
//                OAuthProvider.GOOGLE, googleApiClient,
//                OAuthProvider.KAKAO, kakaoApiClient,
//                OAuthProvider.NAVER, naverApiClient
//        );
//
//        // Create RequestOAuthInfoService instance with mock clients
//        RequestOAuthInfoService requestOAuthInfoService = new RequestOAuthInfoService(List.of(googleApiClient, kakaoApiClient, naverApiClient));
//
//        // Mock behavior for requestAccessToken and requestOauthInfo methods
//        when(googleApiClient.oAuthProvider()).thenReturn(OAuthProvider.GOOGLE);
//        when(googleApiClient.requestAccessToken(ArgumentMatchers.any())).thenReturn("googleAccessToken");
//        when(googleApiClient.requestOauthInfo("googleAccessToken")).thenReturn(new OAuthInfoResponse("Google User"));
//
//        when(kakaoApiClient.oAuthProvider()).thenReturn(OAuthProvider.KAKAO);
//        when(kakaoApiClient.requestAccessToken(ArgumentMatchers.any())).thenReturn("kakaoAccessToken");
//        when(kakaoApiClient.requestOauthInfo("kakaoAccessToken")).thenReturn(new OAuthInfoResponse("Kakao User"));
//
//        when(naverApiClient.oAuthProvider()).thenReturn(OAuthProvider.NAVER);
//        when(naverApiClient.requestAccessToken(ArgumentMatchers.any())).thenReturn("naverAccessToken");
//        when(naverApiClient.requestOauthInfo("naverAccessToken")).thenReturn(new OAuthInfoResponse("Naver User"));
//
//        // Perform the test
//        OAuthLoginParams googleLoginParams = new OAuthLoginParams(OAuthProvider.GOOGLE, "googleCode");
//        OAuthLoginParams kakaoLoginParams = new OAuthLoginParams(OAuthProvider.KAKAO, "kakaoCode");
//        OAuthLoginParams naverLoginParams = new OAuthLoginParams(OAuthProvider.NAVER, "naverCode");
//
//
//        OAuthInfoResponse googleResponse = requestOAuthInfoService.request(googleLoginParams);
//        OAuthInfoResponse kakaoResponse = requestOAuthInfoService.request(kakaoLoginParams);
//        OAuthInfoResponse naverResponse = requestOAuthInfoService.request(naverLoginParams);
//
//        // Assert the results
//        Assertions.assertEquals("Google User", googleResponse.getEmail());
//        Assertions.assertEquals("Kakao User", kakaoResponse.getEmail());
//        Assertions.assertEquals("Naver User", naverResponse.getEmail());
//    }
}
