package com.example.demo.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReadDAO;
import com.example.demo.dao.WriteDAO;
import com.example.demo.dto.BoardDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WriteBoardService {
	
	private final WriteDAO writeBoard;
	private final ReadDAO readBoard;
	
	public WriteBoardService(WriteDAO writeBoard, ReadDAO readBoard) {
		this.writeBoard = writeBoard;
		this.readBoard = readBoard;
	}
	
	
	public ResponseEntity<?> crePosts(Map<String, Object> req){
		
		
		if(req.isEmpty() || req == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		BoardDTO data = new BoardDTO();
		data.setTitle(String.valueOf(req.get("title")));
		data.setContent(String.valueOf(req.get("content")));
		data.setUser_id(Integer.parseInt(readBoard.selectUserId(req.get("username").toString())));
		
		
		writeBoard.InsertPosts(data);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public ResponseEntity<?> updatePosts(Map<String, Object> req){
		
		if(req.isEmpty() || req == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		BoardDTO data = new BoardDTO();
		data.setTitle(String.valueOf(req.get("title")));
		data.setContent(String.valueOf(req.get("content")));
		data.setUser_id(Integer.parseInt(readBoard.selectUserId(req.get("username").toString())));
		data.setPost_id(Integer.parseInt(req.get("post_id").toString()));
		
		writeBoard.UpdatePosts(data);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	public ResponseEntity<?> deletePosts(String id){
		if(id == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		writeBoard.DeletePosts(Integer.parseInt(id));
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
