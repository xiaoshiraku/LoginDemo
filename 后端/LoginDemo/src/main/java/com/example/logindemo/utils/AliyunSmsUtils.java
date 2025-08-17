package com.example.logindemo.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;

/**
 * 阿里云短信服务工具类
 */
public class AliyunSmsUtils {

    // 阿里云访问密钥（需替换为自己的）
    @Value("${aliyun.access-key-id}")
    private static String ACCESS_KEY_ID;
    @Value("${aliyun.access-key-secret}")
    private static String ACCESS_KEY_SECRET = "你的AccessKeySecret";
    // 短信签名（需在阿里云控制台申请并通过审核）
    @Value("${aliyun.sign-name}")
    private static String SIGN_NAME = "你的短信签名";

    // 验证码短信模板CODE（需在阿里云控制台申请并通过审核）
    // 模板内容示例："你的验证码是：${code}，请在5分钟内使用，过期无效。"
    @Value("${aliyun.verify-code-template}")
    private static String VERIFY_CODE_TEMPLATE = "你的验证码模板CODE";

    // 初始化AcsClient（单例模式，避免重复创建）
    private static IAcsClient acsClient;

    static {
        try {
            // 短信API的地域ID，默认是cn-hangzhou
            String regionId = "cn-hangzhou";
            // 初始化客户端
            IClientProfile profile = DefaultProfile.getProfile(regionId, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            DefaultProfile.addEndpoint(regionId, regionId, "Dysmsapi", "dysmsapi.aliyuncs.com");
            acsClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            throw new RuntimeException("初始化阿里云短信客户端失败", e);
        }
    }

    /**
     * 发送验证码短信
     * @param phone 手机号（支持单个号码，格式：13800138000）
     * @param code 验证码内容（如：123456）
     * @return 发送结果（成功/失败原因）
     */
    public static SmsResult sendVerifyCode(String phone, String code) {
        // 1. 构建请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 设置请求方式为POST
        request.setMethod(com.aliyuncs.http.MethodType.POST);
        // 接收短信的手机号
        request.setPhoneNumbers(phone);
        // 短信签名
        request.setSignName(SIGN_NAME);
        // 短信模板CODE
        request.setTemplateCode(VERIFY_CODE_TEMPLATE);
        // 模板变量替换（JSON格式，对应模板中的${code}）
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        try {
            // 2. 调用API发送短信
            SendSmsResponse response = acsClient.getAcsResponse(request);

            // 3. 处理响应结果
            if ("OK".equals(response.getCode())) {
                // 发送成功
                return new SmsResult(true, "短信发送成功", response.getRequestId());
            } else {
                // 发送失败（错误码参考阿里云文档）
                return new SmsResult(false, "发送失败：" + response.getMessage(), response.getRequestId());
            }
        } catch (ClientException e) {
            // 异常处理（如网络错误、参数错误等）
            return new SmsResult(false, "发送异常：" + e.getMessage(), null);
        }
    }

    /**
     * 短信发送结果封装类
     */
    public static class SmsResult {
        private boolean success; // 是否成功
        private String message;  // 消息
        private String requestId; // 阿里云请求ID（用于问题排查）

        public SmsResult(boolean success, String message, String requestId) {
            this.success = success;
            this.message = message;
            this.requestId = requestId;
        }

        // getter和setter
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getRequestId() { return requestId; }
    }
}