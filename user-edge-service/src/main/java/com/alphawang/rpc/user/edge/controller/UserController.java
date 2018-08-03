package com.alphawang.rpc.user.edge.controller;

import com.alphawang.rpc.thrift.user.service.api.UserInfo;
import com.alphawang.rpc.user.edge.response.Response;
import com.alphawang.rpc.user.edge.thrift.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Controller
public class UserController {
    
    @Autowired
    private ServiceProvider serviceProvider;
    
    public Response<String> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
     
        // 1. 验证
        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            log.error("Failed to get UserInfo for {}.", username, e);
            return Response.ERROR;
        }
        
        if (userInfo == null) {
            return Response.ERROR;
        }
        
        if (!userInfo.getPassword().equals(md5(password))) {
            log.error("Password NOT match!");
            return Response.ERROR;
        }
        

        // 2. 生成token
        String token = genToken();
        
        // 3. 缓存用户
        
        return Response.success(null);
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz", 32);
    }

    private String randomCode(String s, int size) {
        StringBuilder sb = new StringBuilder(size);

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int loc = random.nextInt(s.length());
            sb.append(s.charAt(loc));
        }
        return sb.toString();
    }

    private String md5(String pwd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = digest.digest(pwd.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
