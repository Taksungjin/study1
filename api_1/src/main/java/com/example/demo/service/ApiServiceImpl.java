package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ApiDAO;
import com.example.demo.model.response.Response;
import com.example.demo.model.response.ResponseStatus;

@Service
public class ApiServiceImpl implements ApiService{
	
	@Autowired
	private ApiDAO apiDao;
	
	private Response response;
//	private TestVO testDTO;
	
	public Response loginChk(Map<String, Object> map) {
//		testDTO = new TestVO();
		response = new Response();
		try {
			Map<String, Object> user = apiDao.userProve(map);
			if(user != null) {
				if(map.get("id").equals(user.get("id")) && map.get("pwd").equals(user.get("pwd"))) {
					response.setStatus(ResponseStatus.OK.getStatusCode());
					response.setMsg(ResponseStatus.OK.getStatusMessage());
				}
			}else {
				response.setStatus(ResponseStatus.FAIL.getStatusCode());
				response.setMsg(ResponseStatus.FAIL.getStatusMessage());
				System.out.println("쿼리결과가 없거나 조회 실패");
			}
			
		}catch (Exception e) {
//			log.error("EXCEPTION_ERROR {}", e);
            throw new RuntimeException(e);
		}
		
		return response;
	}
	
}
