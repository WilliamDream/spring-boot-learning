/**
 * 
 */
package com.william.springbootSecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.william.springbootSecurity.entity.UserAccount;
import com.william.springbootSecurity.mapper.UserAccountMapper;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2018年8月19日 下午3:46:58
 * @version: v1.0.0
 */
public class UserService implements UserDetailsService{

	@Autowired
	private UserAccountMapper userAccountMapper;
	
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		UserAccount userAccount =  userAccountMapper.getAccountByAccount(s);
		if(userAccount == null)
			throw new UsernameNotFoundException("该用户不存在！");
		return userAccount;
	}

}
