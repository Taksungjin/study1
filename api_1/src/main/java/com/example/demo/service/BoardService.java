package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ApiDAO;
import com.example.demo.dto.BoardDTO;

@Service
public class BoardService {
	
	private final ApiDAO boardRepository;
	
	public BoardService(ApiDAO boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public List<?> boards() {
		
		List<BoardDTO> data = boardRepository.board();
		System.out.println("게시판응답데이터"+data);
		return data;
	}

}
