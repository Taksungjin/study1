package com.example.demo.common;

import jakarta.servlet.http.Cookie;

public class CreateCookie {
	
	public Cookie creCookie(String key, String value) {
		
		Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
	}
}
