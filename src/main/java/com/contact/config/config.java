package com.contact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class config extends WebSecurityConfigurerAdapter{
	
	
	@Bean
	public UserDetailsService getUserDetailService() {
		
		return new userdetailsserviceimpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordencoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordencoder());
		return daoAuthenticationProvider;
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
	auth.authenticationProvider(authenticationProvider());
	
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.httpBasic().and().authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		.antMatchers("/user/**"). hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
		.loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/dash")
		.and().csrf().disable();
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
