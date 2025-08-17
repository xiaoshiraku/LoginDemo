package com.example.logindemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class Jwt {
    @Value("${config.jwt.secret}")
    private String secret;
    @Value("${config.jwt.expire}")
    private long expire;// token 过期时间
    @Value("${config.jwt.header}")
    private String header;

    //生成token
    public String createToken(String subject) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT") //设置token类型为jwt
                .setSubject(subject)//设置载荷，通常是用户ID和用户名
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)//使用签名算法和密钥生成token
                .compact();
    }

    //获取token里的注册信息
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    //判断token是否过期
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    //获取token中的用户信息
    public String getTokenClaims(String token){
        return getTokenClaim(token).getSubject();
    }

    public Date getIssuedAtDate(String token){
        return getTokenClaim(token).getIssuedAt();
    }

}
