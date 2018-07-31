package com.alphawang.rpc.user.service;

import com.alphawang.rpc.thrift.user.UserInfo;
import com.alphawang.rpc.thrift.user.UserService;
import org.apache.thrift.TException;

public class UserServiceImpl implements UserService.Iface {
    @Override 
    public UserInfo getUserById(int id) throws TException {
        return null;
    }

    @Override 
    public UserInfo getUserByName(String username) throws TException {
        return null;
    }

    @Override 
    public void registerUser(UserInfo userInfo) throws TException {

    }
}
