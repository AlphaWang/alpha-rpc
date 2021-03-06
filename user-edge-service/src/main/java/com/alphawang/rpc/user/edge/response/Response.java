package com.alphawang.rpc.user.edge.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    
    public static final Response ERROR = new Response("00", "ERROR");
    
    private String code;
    private String message;
    private T data;
    
    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    } 
    
    public static <T>  Response success(T data) {
        Response<T> response = new Response<>("01", "SUCCESS");
        response.setData(data);
        return response;
    }

    public static <T>  Response fail(T data) {
        Response<T> response = new Response<>("00", "ERROR");
        response.setData(data);
        return response;
    }

    public static Response exception(Exception e) {
        return new Response("9999", e.getMessage());
    }
}
