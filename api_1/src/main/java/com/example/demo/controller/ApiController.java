package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserInfoVO;
import com.example.demo.service.BoardService;
import com.example.demo.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiController {
	
	
	private final ProfileService profileService;
	private final BoardService boardService;
	
	public ApiController(ProfileService profileService, BoardService boardService) {
		this.profileService = profileService;
		this.boardService = boardService;
	}
	
	@GetMapping("/my1")
	@ResponseBody
	public String test() {
		log.info("통과");
		return "로그인됨";
	}

	@GetMapping("/api/board")
	public List<?> board() {
		
		return boardService.boards();
	}
	

	@PostMapping("/api/profile")
	public UserInfoVO profiles(@RequestBody Map<String, Object> req, HttpServletRequest header) {
		
		log.info("프로필 요청 데이터 :" + String.valueOf(req));
		return profileService.profile(req, header);
	}
}
	
