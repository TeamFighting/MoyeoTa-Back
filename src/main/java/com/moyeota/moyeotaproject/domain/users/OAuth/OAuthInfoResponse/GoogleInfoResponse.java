package com.moyeota.moyeotaproject.domain.users.OAuth.OAuthInfoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import lombok.Getter;

@Getter
public class GoogleInfoResponse implements OAuthInfoResponse{

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String picture;


    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public String getProfileImage() {
        return picture;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.GOOGLE;
    }
}
