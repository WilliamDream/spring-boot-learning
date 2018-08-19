/**
 * 
 */
package com.william.springbootSecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.springbootSecurity.entity.UserAccount;
import com.william.springbootSecurity.mapper.UserAccountMapper;
import com.william.springbootSecurity.service.UserAccountService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月16日 下午3:12:32
 * @version: v1.0.0
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountMapper userAccountMapper;
	
	@Override
	public UserAccount getOnetUserAccount(UserAccount userAccount) {
		return userAccountMapper.getOnetUserAccount(userAccount);
	}

}
