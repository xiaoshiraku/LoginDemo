package com.example.logindemo.interceptor;

import com.example.logindemo.utils.Jwt;
import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//jwt认证
@Component
public class CheckTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private Jwt jwt;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行预检请求
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }
        //token验证
        String token = request.getHeader(jwt.getHeader());
        //检查token字符串是否为null或长度为0
        if (StringUtils.isEmpty( token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty( token)){
            throw new SignatureException(jwt.getHeader()+"不能为空");
        }

        Claims claims = null;
        try {
            claims = jwt.getTokenClaim(token);
            if (claims == null || jwt.isTokenExpired(claims.getExpiration())){
                throw new SignatureException(jwt.getHeader()+"失效，请重新登录");
            }
        } catch (Exception e) {
            throw new SignatureException(jwt.getHeader()+"失效，请重新登录");
        }
        request.setAttribute("identityId",claims.getSubject());
        return true;
    }
}