package com.mgmtp.easyquizy.dto.kahoot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KahootUserResponseDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires")
    private Long expireTime;
    @JsonProperty("user")
    private KahootUserInfo kahootUserInfo;
}