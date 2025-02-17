package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SignUpRequestDTO;
import com.example.demo.service.JoinService;
import com.example.demo.service.WriteBoardService;

import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JoinController {
	
	@Autowired
	private JoinService joinService;
	
	@Autowired
	private WriteBoardService writeBoardService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> joinCtl(@RequestBody SignUpRequestDTO req) {
		
//		joinService.join(req);
		log.info("회원정보 데이터 : " + req);
		return joinService.join(req);
	}
	
	@PostMapping("/api/board")
	public ResponseEntity<?> crePosts(@RequestBody Map<String, Object> req){
		
		return writeBoardService.crePosts(req);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseEntity<?> updatePosts(@RequestBody Map<String, Object> req, @PathVariable("id") String id){
		req.put("post_id",id);
		return writeBoardService.updatePosts(req);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseEntity<?> deletePosts(@PathVariable("id") String id){
		
		return writeBoardService.deletePosts(id);
	}
	

}
