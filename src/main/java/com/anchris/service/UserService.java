package com.anchris.service;

import com.anchris.dao.UserMapper;
import com.anchris.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
