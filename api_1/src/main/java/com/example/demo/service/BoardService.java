package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ReadDAO;
import com.example.demo.dto.BoardDTO;

@Service
public class BoardService {
	
	private final ReadDAO boardRepository;
	
	public BoardService(ReadDAO boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public List<?> boards() {
		
		List<BoardDTO> data = boardRepository.board();
		System.out.println("게시판응답데이터"+data);
		return data;
	}

}
