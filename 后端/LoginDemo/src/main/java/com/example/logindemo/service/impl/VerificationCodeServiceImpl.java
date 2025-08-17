package com.example.logindemo.service.impl;

import com.example.logindemo.service.VerificationCodeService;
import com.example.logindemo.utils.AliyunSmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Value("${app.verification.code.length}")
    private int codeLength;
    @Value("${app.verification.code.expire}")
    private long expireMinutes;
    @Value("${app.verification.code.send-interval}")
    private int sendInterval;

    // Redis键前缀
    private static final String CODE_PREFIX = "verify:code:";
    private static final String SEND_INTERVAL_PREFIX = "verify:interval:";

    /**
     * 生成验证码并发送
     *
     * @param phone
     */
    @Override
    public boolean sendCode(String phone) {
        //检查发送间隔
        String intervalKey = SEND_INTERVAL_PREFIX + phone;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(intervalKey))) {
            return false;
        }
        //生成验证码
        String code = generateCode(codeLength);

        // 发送验证码（这里会调用短信服务API）
        AliyunSmsUtils.SmsResult result = AliyunSmsUtils.sendVerifyCode(phone, code);
        if (result.isSuccess()) {
            System.out.println("发送成功：" + result.getMessage());
            // 存储验证码到Redis，设置过期时间
            String codeKey = CODE_PREFIX + phone;
            stringRedisTemplate.opsForValue().set(codeKey, code, expireMinutes, java.util.concurrent.TimeUnit.MINUTES);
            //设置发送间隔
            stringRedisTemplate.opsForValue().set(intervalKey, "1", sendInterval, java.util.concurrent.TimeUnit.MINUTES);
            return true;
        } else {
            // 记录错误日志，用于排查问题
            System.err.println("发送失败：" + result.getMessage());
        }
        //System.out.println("向手机号: " + phone + " 发送验证码: " + code);
        return false;
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     */
    @Override
    public boolean verifyCode(String phone, String code) {
        String codeKey = CODE_PREFIX + phone;
        //获取redis中的验证码
        String redisCode = stringRedisTemplate.opsForValue().get(codeKey);
        if (redisCode != null && redisCode.equals(code)) {
            //如果验证码正确，删除验证码
            stringRedisTemplate.delete(codeKey);
            return true;
        }
        return false;
    }

    // 生成随机验证码
    private String generateCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
