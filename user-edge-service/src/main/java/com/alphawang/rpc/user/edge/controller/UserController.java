package com.alphawang.rpc.user.edge.controller;

import com.alphawang.rpc.thrift.user.service.api.UserInfo;
import com.alphawang.rpc.thrift.user.service.api.dto.UserDto;
import com.alphawang.rpc.user.edge.redis.RedisClient;
import com.alphawang.rpc.user.edge.response.Response;
import com.alphawang.rpc.user.edge.thrift.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private ServiceProvider serviceProvider;
    @Autowired
    private RedisClient redisClient;
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    
    @PostMapping("/login")
    @ResponseBody
    public Response<String> login(
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
     
        // 1. 验证
        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            log.error("Failed to get UserInfo for {}.", username, e);
            return Response.fail(e.getMessage());
        }
        
        if (userInfo == null) {
            return Response.fail("user null");
        }
        
        if (!userInfo.getPassword().equals(md5(password))) {
            log.error("Password NOT match!");
            return Response.fail("PASSWORD ERROR.");
        }
        

        // 2. 生成token
        String token = genToken();
        log.info("token for {} : {}", username, token);
        
        // 3. 缓存用户
        UserDto dto = toDto(userInfo);
        redisClient.set(token, dto, 3600);
        log.info("save redis for {} : {} = {}", username, token, dto);
        
        return Response.success(token);
    }
    
    @PostMapping("/sendVerifyCode")
    @ResponseBody
    public Response sendVerifyCode(
        @RequestParam(value="mobile", required = false) String mobile,
        @RequestParam(value="email", required = false) String email) {
        
        String message = "verify code is:";
        String code = randomCode("0123456789", 6);
        
        log.warn("CODE {}", code);
        return mockSend(mobile, email, message, code);
    }

    private Response mockSend(String mobile, String email, String message, String code) {
        if (StringUtils.isNotBlank(mobile)) {
            redisClient.set(mobile, code);
        } else if (StringUtils.isNotBlank(email)) {
            redisClient.set(email, code);
        } else {
            return Response.fail("mobile or email should not empty");
        }

        return Response.success(code);  //just for testing, should not send code in response.
    }
    
    private Response send(String mobile, String email, String message, String code) {
        try {
            boolean result = false;
            if (StringUtils.isNotBlank(mobile)) {
                result = serviceProvider.getMessageService().sendMobileMessage(mobile, message + code);
                redisClient.set(mobile, code);
            } else if (StringUtils.isNotBlank(email)) {
                result = serviceProvider.getMessageService().sendEmailMessage(email, message + code);
                redisClient.set(email, code);
            } else {
                return Response.fail("mobile or email should not empty");
            }

            if (!result) {
                return Response.fail("send msg fail");
            }
        } catch (TException e) {
            e.printStackTrace();
            return Response.fail("send msg fail");
        }

        return Response.success(code);  //just for testing, should not send code in response.
    }
    
    @PostMapping("/register")
    @ResponseBody
    public Response<String> register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
        @RequestParam(value="mobile", required = false) String mobile,
        @RequestParam(value="email", required = false) String email,
        @RequestParam("verifyCode") String verifyCode) {
        
        if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)) {
            return Response.fail("mobile or email should not empty");
        }
        if (StringUtils.isEmpty(verifyCode)) {
            return Response.fail("code should not empty");
        }
        
        String code;
        if (StringUtils.isNotBlank(mobile)) {
            code  = redisClient.get(mobile);
        } else {
            code = redisClient.get(email);
        }
        
        if (!verifyCode.equals(code)) {
            return Response.fail("code not match.");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);

        try {
            serviceProvider.getUserService().registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }

        return Response.success("");
        
    }
    
    @PostMapping("/authentication")
    @ResponseBody
    public UserDto auth(@RequestHeader("token") String token) {
        return redisClient.get(token);
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

    /**
     * UserInfo 中有太多不必要的对象
     * @param userInfo
     * @return
     */
    private UserDto toDto(UserInfo userInfo) {
        UserDto dto = new UserDto();
//        dto.setId(userInfo.getId());
//        dto.setEmail(userInfo.getEmail());
//        dto.setPassword(userInfo.getPassword());
//        dto.setRealName(userInfo.getRealName());
//        dto.setMobile(userInfo.getMobile());
        BeanUtils.copyProperties(userInfo, dto);
        
        return dto;
    }
    
}
