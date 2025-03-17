package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReadDAO;
import com.example.demo.dao.WriteDAO;
import com.example.demo.dto.CustomOAuth2User;
import com.example.demo.dto.GoogleResponse;
import com.example.demo.dto.NaverResponse;
import com.example.demo.dto.OAuth2Response;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserInfoVO;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private ReadDAO apiDao;
	
	@Autowired
	private WriteDAO joinDAO;
	
	 @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

	        OAuth2User oAuth2User = super.loadUser(userRequest);


	        String registrationId = userRequest.getClientRegistration().getRegistrationId();
	        OAuth2Response oAuth2Response = null;
	        if (registrationId.equals("naver")) {

	            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
	        }
	        else if (registrationId.equals("google")) {

	            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
	        }
	        else {

	            return null;
	        }
	        
    		//리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
	        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
	        UserInfoVO existData = apiDao.selectId(username);

	        if(existData == null) {
	        	
	        	UserInfoVO userEntity = new UserInfoVO();
	        	userEntity.setUsername(username);
	        	userEntity.setEmail(oAuth2Response.getEmail());
	        	userEntity.setName(oAuth2Response.getName());
//	        	userEntity.setRole("ROLE_USER");
	        	
	        	joinDAO.oAuth2UserInsert(userEntity);
	        	
	        	
	        	UserDTO userDTO = new UserDTO();
	        	userDTO.setUsername(username);
	        	userDTO.setName(oAuth2Response.getName());
//	        	userDTO.setRole("ROLE_USER");
	        	
	        	return new CustomOAuth2User(userDTO);
	        	
	        }else {
	        	
	        	existData.setEmail(oAuth2Response.getEmail());
	        	existData.setName(oAuth2Response.getName());
	        	
	        	joinDAO.oAuth2UserUpdate(existData);
	        	
	        	
	        	UserDTO userDTO = new UserDTO();
	        	userDTO.setUsername(existData.getUsername());
	        	userDTO.setName(existData.getName());
	        	userDTO.setRole(existData.getRole());
	        	
	        	return new CustomOAuth2User(userDTO);
	        	
	        	
	        }
	        
	 }
}
