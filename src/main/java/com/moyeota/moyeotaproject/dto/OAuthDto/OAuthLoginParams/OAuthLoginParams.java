package com.moyeota.moyeotaproject.dto.OAuthDto.OAuthLoginParams;

import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
    OAuthProvider oAuthProvider();
    MultiValueMap<String, String> makeBody();
}
