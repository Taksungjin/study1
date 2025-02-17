package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ApiDAO;
import com.example.demo.dao.JoinDAO;
import com.example.demo.dto.SignUpRequestDTO;
import com.example.demo.dto.UserInfoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JoinService {
	
	@Autowired 
	private JoinDAO joinDAO;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Autowired
	private ApiDAO apiDAO;
	
	public ResponseEntity<?> join(SignUpRequestDTO req) {
		
		UserInfoVO idChk = apiDAO.selectId(req.getUsername());
		
		try {
			if(idChk != null && req.getUsername().equals(idChk.getUsername())) {
				
				log.info("아이디 중복");
				return ResponseEntity.status(400).body("아이디 중복");
			}else {
				
				SignUpRequestDTO data = new SignUpRequestDTO();
				
				data.setUsername(req.getUsername());
				data.setPhone(req.getPhone());
				data.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
				data.setEmail(req.getEmail());
				data.setName(req.getName());
				if(req.getIs_admin()!=null && String.valueOf(req.getIs_admin()).equals("true")) {
					data.setIs_admin(true);
				}
				joinDAO.userInsert(data);
				
				log.info("회원가입 정보 {}",data);
			}
		}catch (Exception e) {
			log.error("EXCEPTION_ERROR {}", e);
		}
		
		return ResponseEntity.ok("회원가입 성공");
		
	}

}
