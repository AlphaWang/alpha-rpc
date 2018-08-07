package com.alphawang.rpc.user.edge.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    
    private int id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private String realName;
    
}
