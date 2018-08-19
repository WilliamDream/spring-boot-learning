/**
 * 
 */
package com.william.springbootSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.springbootSecurity.entity.UserAccount;
import com.william.springbootSecurity.service.UserAccountService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月19日 下午2:21:49
 * @version: v1.0.0
 */
@RestController
@RequestMapping("/acc")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;
	
	@GetMapping("/login")
	public UserAccount userLogin(@RequestBody UserAccount userAccount) {
		return userAccountService.getOnetUserAccount(userAccount);
	}
	
}
