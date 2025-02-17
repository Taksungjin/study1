package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.RefreshDTO;
import com.example.demo.dto.UserInfoVO;

@Mapper
@Repository
public interface ApiDAO {
	
	public UserInfoVO selectId(String req);
	
	public UserInfoVO chkInfo(String req);
	
	public Map<String, Object> userProve(Map<String, Object> map);
	
	public RefreshDTO selectRefresh(String refresh);

	public Map<String, Object> profile(Map<String, Object> map);
	
	public List<BoardDTO> board();
	
	public String selectUserId(String name);
	
	public String findByName(String username); 
}
