package com.aiteam.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer code;

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setCode(200);
        return response;
    }

    public static <T> Response<T> success(T data, String message) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }

    public static <T> Response<T> success(String message) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setCode(200);
        return response;
    }

    public static <T> Response<T> error(String message) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(500);
        return response;
    }

    public static <T> Response<T> error(String message, Integer code) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(code);
        return response;
    }

    public static <T> Response<T> error(String message, Integer code, T data) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setCode(code);
        response.setData(data);
        return response;
    }
}