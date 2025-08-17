package com.example.logindemo.controller;


import com.example.logindemo.domain.User;
import com.example.logindemo.service.UserService;
import com.example.logindemo.service.VerificationCodeService;
import com.example.logindemo.utils.Jwt;
import com.example.logindemo.utils.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name="权限认证接口")
public class LoginController {
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private Jwt jwt;


    // 发送验证码
    @PostMapping("/send-code")
    @Operation(summary ="发送验证码")
    public ResultVo sendCode(@RequestBody Map<String, Object> request){
        // 获取请求中的手机号
         String phone = (String) request.get("phone");
         //手机号校验：手机号是否为空
         if(phone == null || phone.isEmpty()){
             return ResultVo.fail("手机号不能为空");
         }
         boolean result = verificationCodeService.sendCode(phone);
         if(result){
             return ResultVo.ok("验证码发送成功");
         }else{
             return ResultVo.fail("验证码发送失败");
         }
    }


    @PostMapping("/login")
    @Operation(summary ="登录")
    public ResultVo login(@RequestBody Map<String, Object> request){
        String phone = request.get("phone").toString();
        String code = request.get("code").toString();
        if(phone == null || phone.isEmpty() || code == null || code.isEmpty()){
            return ResultVo.fail("手机号或验证码不能为空");
        }
        //校验验证码
       boolean result = verificationCodeService.verifyCode(phone, code);
        if(! result){
            return ResultVo.fail("验证码错误");
        }
        //注册用户
        User user = userService.findByPhone(phone);
        if(user == null){
            user = userService.register(phone);
        }
        String token = jwt.createToken(user.getNickname());
        return ResultVo.ok(user,token);
    }


}
