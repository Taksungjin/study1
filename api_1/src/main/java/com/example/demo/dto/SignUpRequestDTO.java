package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDTO {
	
	@NotBlank(message = "사용자 이름은 필수입니다")
//	@Size(min = 3, max = 50)
	private String username;
	
	@NotBlank(message = "이메일은 필수입니다")
	@Email(message = "올바른 이메일 형식이 아닙니다")
	private String email;
	
	@NotBlank(message = "비밀번호는 필수입니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
    message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다")
	private String password;
	
	@NotBlank(message = "휴대폰번호는 필수입니다")
	private String phone;
	
	private Boolean is_active;
	private Boolean is_admin;
	
	private String name;
}
