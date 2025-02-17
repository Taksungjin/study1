package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ApiDAO;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.UserInfoVO;
import com.example.demo.model.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private ApiDAO apiDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Response response;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		response = new Response();
		try {
			UserInfoVO userInfo = apiDao.chkInfo(username);
			
			if(userInfo != null) {
//				if(map.get("id").equals(user.get("id")) && map.get("pwd").equals(user.get("pwd"))) {
//					response.setStatus(ResponseStatus.OK.getStatusCode());
//					response.setMsg(ResponseStatus.OK.getStatusMessage());
				if(userInfo.getRole() != null && userInfo.getRole().equals("t")) {
					userInfo.setRole("ADMIN");
				}else if(userInfo.getRole() != null && userInfo.getRole().equals("f"))
				{
					userInfo.setRole("USER");
				}
				log.info("아이디 조회 결과 : {}",userInfo);
				return new CustomUserDetails(userInfo);
//				}
			}else {
//				response.setStatus(ResponseStatus.FAIL.getStatusCode());
//				response.setMsg(ResponseStatus.FAIL.getStatusMessage());
				log.debug("조회 결과가 없거나 조회 실패");
			}
			
		}catch (Exception e) {
//			log.error("EXCEPTION_ERROR {}", e);
            throw new RuntimeException(e);
		}
		
		throw new UsernameNotFoundException("UsernameNotFoundException!!");
	}

}
