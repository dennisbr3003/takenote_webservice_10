package com.notemaster.android.ws.v1.notemasterweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//super.configure(http); <-- this has to go or it will not work
		
        http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","/home","/error","/login","/notemaster/ping","/notemaster/userdata/adduser").permitAll()
		.anyRequest().authenticated()
		.and()
		.httpBasic(); //<-- you will need this to get basic authorization to work
        
	}
		
    @Bean
    public UserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService(); // <-- this will check the user in the database
    }    
    

}
