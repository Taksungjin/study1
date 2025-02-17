package com.example.demo.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.demo.dao.ApiDAO;
import com.example.demo.dao.JoinDAO;
import com.example.demo.jwt.CustomLogoutFilter;
import com.example.demo.jwt.JWTFilter;
import com.example.demo.jwt.JWTUtil;
import com.example.demo.jwt.LoginFilter;
import com.example.demo.oauth2.CustomSuccessHandler;
import com.example.demo.service.CustomOAuth2UserService;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JWTUtil jwtUtil;
	private final JoinDAO joinDao;
	private final ApiDAO apiDAO;
	
	
	//생성자주입
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JoinDAO joinDao, ApiDAO apiDAO) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
		this.customOAuth2UserService = customOAuth2UserService;
		this.customSuccessHandler = customSuccessHandler;
		this.joinDao = joinDao;
		this.apiDAO = apiDAO;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.cors((cors) -> cors
				.configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest req) {
				
				CorsConfiguration configuration = new CorsConfiguration();
				
				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
				configuration.setAllowedMethods(Collections.singletonList("*"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));
				configuration.setMaxAge(3600L);
//				configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//				configuration.setExposedHeaders(Collections.singletonList("Authorization"));
				configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "access"));
				
				return configuration;
			}
		}));
		
		//csrf disable
		http.csrf((auth) -> auth.disable());
		
		//form 로그인 방식 disable
		http.formLogin((auth) -> auth.disable());
		
		//http basic 인증방식 disable
		http.httpBasic((auth) -> auth.disable());
		
//		http.logout((auth) -> auth.disable());
		
		//oauth2
		http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));
		
		//경로별 인가 작업
		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/login", "/","/signup").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/reissue", "/return", "/sms").permitAll()
				.requestMatchers("/api/board").permitAll()
				.anyRequest().authenticated());
		
//		http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
		http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
		
		//필터등록
		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, joinDao), UsernamePasswordAuthenticationFilter.class);
		
		
		http.addFilterBefore(new CustomLogoutFilter(jwtUtil, apiDAO, joinDao), LogoutFilter.class);
		
		//세션 설정
		http.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		
		return http.build();
	}
}
