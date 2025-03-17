package com.example.demo.oauth2;

import java.io.IOException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dao.WriteDAO;
import com.example.demo.dto.CustomOAuth2User;
import com.example.demo.dto.RefreshDTO;
import com.example.demo.jwt.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final WriteDAO joinDAO;

    public CustomSuccessHandler(JWTUtil jwtUtil, WriteDAO joinDAO) {

        this.jwtUtil = jwtUtil;
        this.joinDAO = joinDAO;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
//        String token = jwtUtil.createJwt(username, role, 60*60*60L);
        
//        String access = jwtUtil.createJwt("access", username, role, 6000000L);
		String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
		
		//refresh token 저장
		addRefreshEntity(username, refresh, 86400000L);
		
        response.addCookie(createCookie("refresh", refresh));
        response.sendRedirect("http://3.39.154.169/reissue");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
		
		Date date = new Date(System.currentTimeMillis() + expiredMs);
		RefreshDTO refreshDTO = new RefreshDTO();
		
		refreshDTO.setUsername(username);
		refreshDTO.setRefresh(refresh);
		refreshDTO.setExpiration(date.toString());
		
		joinDAO.refreshInsert(refreshDTO);
	}
}