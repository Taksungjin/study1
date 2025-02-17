package com.example.demo.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.example.demo.dao.JoinDAO;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.RefreshDTO;
import com.example.demo.dto.UserInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter{
	
	
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final JoinDAO joinDAO;
	
	//생성자주입
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, JoinDAO joinDAO) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.joinDAO = joinDAO;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException{
		

		UserInfoVO userInfoVO = new UserInfoVO();
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ServletInputStream input = req.getInputStream();
			String messageBody = StreamUtils.copyToString(input, StandardCharsets.UTF_8);
			userInfoVO = objectMapper.readValue(messageBody, UserInfoVO.class);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//클라이언트 요청에서 아이디 비빌번호 추출(form-data)
//		String id = obtainUsername(req);
//		String pwd = obtainPassword(req);

		String id = userInfoVO.getUsername();
		String pwd = userInfoVO.getPassword();
		
		//스프링 시큐리티에서 id와 pwd를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, pwd, null);
		
		
		//token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}
	
	//로그인 성공시 실행 메소드 (jwt발급)
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authentication) {
		

		
		String username = authentication.getName();
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();
		
		String access = jwtUtil.createJwt("access", username, role, 6000000L);
		String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
		
		//refresh token 저장
		addRefreshEntity(username, refresh, 86400000L);
		
		res.setHeader("access", access);
		res.addCookie(createCookie("refresh", refresh));
		
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) {
		System.out.println("아이디 또는 비밀번호 불일치");
		res.setStatus(401);
	}
	
	private void addRefreshEntity(String username, String refresh, Long expiredMs) {
		
		Date date = new Date(System.currentTimeMillis() + expiredMs);
		RefreshDTO refreshDTO = new RefreshDTO();
		
		refreshDTO.setUsername(username);
		refreshDTO.setRefresh(refresh);
		refreshDTO.setExpiration(date.toString());
		
		joinDAO.refreshInsert(refreshDTO);
	}
	
	
	
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
	
}
