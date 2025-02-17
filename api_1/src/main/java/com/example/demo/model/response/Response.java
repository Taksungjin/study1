package com.example.demo.model.response;

import java.util.List;

import com.example.demo.dto.UserInfoVO;

import lombok.Data;

@Data
public class Response {
	
	private int status;
	private List<UserInfoVO> userInfo;
	private String msg;
	private List<Object> data;
}
