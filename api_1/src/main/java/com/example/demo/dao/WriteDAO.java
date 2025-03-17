package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.RefreshDTO;
import com.example.demo.dto.SignUpRequestDTO;
import com.example.demo.dto.UserInfoVO;

@Mapper
@Repository
public interface WriteDAO {
	
	int userInsert(SignUpRequestDTO req);
	
	int roleInsert(UserInfoVO req);
	
	int oAuth2UserInsert(UserInfoVO req);
	
	int oAuth2UserUpdate(UserInfoVO req);
	
	int refreshInsert(RefreshDTO req);
	
	void refreshDelete(String refresh);
	
	int InsertPosts(BoardDTO req);
	
	int UpdatePosts(BoardDTO req);
	
	int DeletePosts(int id);
}
