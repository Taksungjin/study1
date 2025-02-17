package com.example.demo.service;

import java.util.Map;

import com.example.demo.model.response.Response;

public interface ApiService {
	
	public Response loginChk(Map<String, Object> map);
}
