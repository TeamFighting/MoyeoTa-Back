package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.Getter;

@Getter
public class GoogleInfoResponse implements OAuthInfoResponse{

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getProfileImage() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return null;
    }
}
