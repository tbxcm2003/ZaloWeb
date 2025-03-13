package com.zalo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String profileName;
    private String avatarUrl;
    private boolean success;
    private String message;
}