package com.example.template.util;

public interface AccessTokenListener {
    public void onAccessTokenReceived(String token);
    public void onAccessTokenError(Exception exception);
}
