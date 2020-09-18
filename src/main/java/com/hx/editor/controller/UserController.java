package com.hx.editor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hx.editor.domain.User;
import com.hx.editor.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/findAll")
	public List<User> findAllUsers(){
		List<User> users=userService.findAllUsers();
		return users;
	}

}
