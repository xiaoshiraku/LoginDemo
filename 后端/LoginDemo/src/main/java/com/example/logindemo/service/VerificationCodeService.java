package com.example.logindemo.service;

public interface VerificationCodeService {
    /**
     * 生成验证码并发送
     */
    boolean sendCode(String phone);

    /**
     * 验证验证码
     */
    boolean verifyCode(String phone, String code);
}