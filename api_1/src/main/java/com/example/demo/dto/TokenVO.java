package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenVO {
	private String grantType; //jwt인증타입
    private String accessToken;
    private String refreshToken;
}
