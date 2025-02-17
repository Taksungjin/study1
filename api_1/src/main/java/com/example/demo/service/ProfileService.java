package com.example.demo.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ApiDAO;
import com.example.demo.dto.UserInfoVO;
import com.example.demo.jwt.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProfileService {
	
	private final ApiDAO profileData;
	private final JWTUtil jwtUtil;
	
	public ProfileService(ApiDAO profileData, JWTUtil jwtUtil) {
		this.profileData = profileData;
		this.jwtUtil = jwtUtil;
	}
	
	
	
	public UserInfoVO profile(Map<String, Object> req, HttpServletRequest header) {
		
		
		String acc = header.getHeader("access");
		
		
		req.put("username", jwtUtil.getUsername(acc));
		Map<String, Object> data = profileData.profile(req);
		UserInfoVO resData = new UserInfoVO();
		resData.setName(data.get("name").toString());
		resData.setEmail(data.get("email").toString());
		resData.setCreated_at(data.get("created_at").toString());
		log.info("프로필 응답 데이터 : " + resData);
		return resData;
	}
}
