package com.example.demo.dto;

import lombok.Data;

@Data
public class UserInfoVO {
	private String user_id;
	private String nickname;
	private String email;
	private String username;
	private String password;
	private String created_at;
	private String updated_at;
	private String last_login;
	private Boolean is_active;
	private String role;
	private String phone;
	private String name;
}
