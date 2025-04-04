package com.example.demo.service;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.common.CreateCookie;
import com.example.demo.dao.ReadDAO;
import com.example.demo.dao.WriteDAO;
import com.example.demo.dto.RefreshDTO;
import com.example.demo.jwt.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReTokenService {

	private final JWTUtil jwtUtil;
	private ReadDAO refreshDao;
	private WriteDAO refreshToken;

    public ReTokenService(JWTUtil jwtUtil, ReadDAO refreshDao, WriteDAO refreshToken) {

        this.jwtUtil = jwtUtil;
        this.refreshDao = refreshDao;
        this.refreshToken = refreshToken;
    }
	
	public ResponseEntity<?> reToken(HttpServletRequest request, HttpServletResponse response) {
		
		//get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {

            //response status code 프론트와 협의한 코드
            return new ResponseEntity<>("token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid token", HttpStatus.BAD_REQUEST);
        }
        
//        Boolean isExist = refreshDao.selectRefresh(refresh) != null ? true : false;
//        if(!isExist) {
//        	return new ResponseEntity<>("invalid token",HttpStatus.BAD_REQUEST);
//        }
        
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        
        
        //기존 refresh토큰 삭제 후 새로운 토큰으로 저장
        refreshToken.refreshDelete(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);
        
        
        //response
        response.setHeader("access", newAccess);
        CreateCookie createCookie = new CreateCookie();
        response.addCookie(createCookie.creCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private void addRefreshEntity(String username, String refresh, Long expiredMs) {
		
		Date date = new Date(System.currentTimeMillis() + expiredMs);
		RefreshDTO refreshDTO = new RefreshDTO();
		
		refreshDTO.setUsername(username);
		refreshDTO.setRefresh(refresh);
		refreshDTO.setExpiration(date.toString());
		
		refreshToken.refreshInsert(refreshDTO);
	}
	
	
}
