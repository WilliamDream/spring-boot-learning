/**
 * 
 */
package com.william.springbootSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.william.springbootSecurity.service.UserAccountService;
import com.william.springbootSecurity.service.impl.UserAccountServiceImpl;

/**
 * @author: william
 * @Description: 继承 WebSecurityConfigurerAdapter，并重写内置的方法
 * @date: 2018年8月15日 下午1:57:28
 * @version: v1.0.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.authorizeRequests()	// 定义那些url需要被保护，那些url不需要保护
		.antMatchers("/login").permitAll()// 定于不需要认证权限的url
		.anyRequest().authenticated() //其他所有资源都需要认证，登陆后访问
		.antMatchers("/home").hasAuthority("ADMIN") // 登录后之后拥有“ADMIN”权限才可以访问/hello方法，否则系统会出现“403”权限不足的提示
		.and()
		.formLogin()
		.loginPage("/login")// 定以登录页是url
		.defaultSuccessUrl("/welcome")	// 登录成功后默认跳转到
		.permitAll()
		.successHandler(loginSuccessHandler()) //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
		.and()
		.logout()
		.logoutSuccessUrl("/login") //退出登录后的默认网址是”/login”
		.permitAll()
		.invalidateHttpSession(true)
		.and()
		.rememberMe()//登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
		.tokenValiditySeconds(1209600);
		
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {	
		//指定密码加密所使用的加密器为passwordEncoder()
		//需要将密码加密后写入数据库 
		auth.userDetailsService(systemUserService()).passwordEncoder(passwordEncoder());
		auth.eraseCredentials(false);		
	}
 
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
 
	@Bean
	public LoginSuccessHandler loginSuccessHandler(){
		return new LoginSuccessHandler();
	}
	
	@Bean
    public UserAccountService systemUserService() {
        return new UserAccountServiceImpl();
    }
	
}
