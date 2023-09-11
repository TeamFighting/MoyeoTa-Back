package com.moyeota.moyeotaproject.component.OAuth.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import org.springframework.util.MultiValueMap;

// 요청에 필요한 데이터를 갖고 있는 파라미터
public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
