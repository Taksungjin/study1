package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ReTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ReTokenConroller {
	
	@Autowired
	private ReTokenService reTokenService;
	
	@PostMapping("/return")
	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
		
		
		return reTokenService.reToken(request, response);
	}
}
