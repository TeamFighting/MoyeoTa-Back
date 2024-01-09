package com.moyeota.moyeotaproject.controller.dto;

import com.moyeota.moyeotaproject.domain.oAuth.OAuthProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthEmailDto {
    private String oauth;
    private String email;
}
