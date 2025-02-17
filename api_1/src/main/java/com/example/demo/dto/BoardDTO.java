package com.example.demo.dto;

import lombok.Data;

@Data
public class BoardDTO {
	
	private String title;
	private String content;
	private String created_at;
	private String updated_at;
	private String name;
	private int post_id;
	private int user_id;

}
