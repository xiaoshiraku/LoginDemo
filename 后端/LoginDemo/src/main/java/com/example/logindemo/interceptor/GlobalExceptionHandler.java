package com.example.logindemo.interceptor;

import com.example.logindemo.utils.ResultVo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 处理token异常
@ControllerAdvice // 监听controller处理器方法是否发生异常
public class GlobalExceptionHandler {

    // 定义异常处理器方法
    // token的非法操作
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResultVo SignatureExceptionHandler(SignatureException e) {
        return ResultVo.fail("token的非法操作，请自重", "token_error");
    }

    // token的解析异常
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseBody
    public ResultVo MalformedJwtExceptionHandler(MalformedJwtException e) {
        return ResultVo.fail("token的解析异常", "token_error");
    }

    // token的过期异常
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResultVo ExpiredJwtExceptionHandler(ExpiredJwtException e) {
        return ResultVo.fail("登陆过时，请重新登录", "token_error");
    }

    // 其他异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo ExceptionHandler(Exception e) {
        if (e.getMessage().contains("token")) {
            return ResultVo.fail(e.getMessage(), "token_error");
        }
        return ResultVo.fail(e.getMessage());
    }

}