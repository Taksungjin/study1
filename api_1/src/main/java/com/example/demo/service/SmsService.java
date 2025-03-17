package com.example.demo.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;


@Service
public class SmsService {
	
	@Value("${coolsms.api.key}")
	private String apiKey;
	
	@Value("${coolsms.api.secret}")
	private String apiSecret;
	
	@Value("${coolsms.api.fromnumber}")
	private String fromNumber;
	
	final DefaultMessageService messageService;
	
	public SmsService() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSJRCI7OMOT2NUY", "XVQV7QGMIA0JFRUYHOQETQ2DFBEKNZWQ", "https://api.coolsms.co.kr");
    }
	
	 public String sendSms(String to) {
		 Message message = new Message();
	        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		 	String authNum = generateRandomNumber();
		 	
	        message.setFrom(fromNumber);
	        message.setTo(to);
	        message.setText("인증코드는 " + authNum + "입니다");

	        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
	        System.out.println(response);
	        if(response.getStatusCode().equals("2000")) {
	        	return authNum;
	        } else {
	        	return "인증번호 발송 오류";
	        }
	        
	    }

	    // 랜덤한 4자리 숫자 생성 메서드
	    private String generateRandomNumber() {
	        Random rand = new Random();
	        StringBuilder numStr = new StringBuilder();
	        for (int i = 0; i < 4; i++) {
	            numStr.append(rand.nextInt(10));
	        }
	        return numStr.toString();
	    }
}
