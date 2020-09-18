package com.hx.editor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.editor.dao.IUserDao;
import com.hx.editor.domain.User;
import com.hx.editor.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserDao userDao;
	
	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

}
