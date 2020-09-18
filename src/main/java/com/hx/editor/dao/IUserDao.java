package com.hx.editor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hx.editor.domain.User;
@Repository("userDao")
public interface IUserDao extends JpaRepository<User, Long> {


}
