/**
 * 
 */
package com.william.springbootSecurity.mapper;

import com.william.springbootSecurity.entity.UserAccount;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月19日 下午2:14:23
 * @version: v1.0.0
 */
public interface UserAccountMapper {

	public UserAccount getAccountByAccount(String account);
	
	public UserAccount getOnetUserAccount(UserAccount userAccount);
	
}
