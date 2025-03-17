package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReadDAO;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.dto.UserInfoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private ReadDAO apiDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		

		try {
			UserInfoVO userInfo = apiDao.chkInfo(username);
			
			if(userInfo != null) {
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
				log.debug("조회 결과가 없거나 조회 실패");
			}
			
		}catch (Exception e) {
//			log.error("EXCEPTION_ERROR {}", e);
            throw new RuntimeException(e);
		}
		
		throw new UsernameNotFoundException("UsernameNotFoundException!!");
	}

}
