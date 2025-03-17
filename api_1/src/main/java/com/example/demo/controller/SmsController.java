package com.example.demo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.SmsService;

@RestController
public class SmsController {
	
	private final SmsService smsService;
	
	public SmsController(SmsService smsService) {
		this.smsService = smsService;
		
	}
	
	@PostMapping("/sms")
	public String smsAuth(@RequestBody Map<String, Object> req) {
		String phone = req.get("phone").toString();
		
		
		return smsService.sendSms(phone);
	}
}
