package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import org.springframework.util.MultiValueMap;

// 카카오, 네이버 요청에 필요한 데이터를 갖고 있는 파라미터
public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
