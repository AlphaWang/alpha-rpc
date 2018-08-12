package com.alphawang.rpc.user.service;

import com.alphawang.rpc.thrift.user.service.api.UserInfo;
import com.alphawang.rpc.thrift.user.service.api.UserService;
import com.alphawang.rpc.user.mapper.UserMapper;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService.Iface {
    
    @Autowired
    private UserMapper userMapper;
    
    
    @Override 
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override 
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    @Override 
    public UserInfo getTeacherById(int id) throws TException {
        return userMapper.getTeacherById(id);
    }

    @Override 
    public void registerUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}
