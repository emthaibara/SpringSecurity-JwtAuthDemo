package com.securityserviceprovider.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

import static com.auth0.jwt.impl.PublicClaims.ISSUER;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/3
 */
public class JwtUtil {

    private volatile static String SALT;

    //jwt的规定，最后请求的格式应该是 `Bearer token`
    public static final String TOKENPREFIX = "Bearer ";

    public static final String TOKENHEADER = "token";

    //设置过期失效
    private static final long EXPIREDTIME = 1728000000L;

    public JwtUtil() {

    }

    public static String getUserRole(String token) {
        return JwtUtil.getJWTVerifier(token).verify(token).getClaim("role").asString();
    }

    //生成token
    public static String generateToken(UserDetails user) {
        SALT = BCrypt.gensalt();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUsername())    //设置主题
                .withClaim("role",getRole(user))
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIREDTIME)) //过期失效
                .withIssuedAt(new Date())       //发行时间
                .sign(Algorithm.HMAC256(SALT));
    }

    private static String getRole(UserDetails user) {
        for (GrantedAuthority authority : user.getAuthorities()){
            return authority.getAuthority();
        }
        return null;
    }

    public static String getUsername(String token){
            return getJWTVerifier(SALT).verify(token).getSubject();
    }

    private static JWTVerifier getJWTVerifier(String salt){
        return JWT.require(Algorithm.HMAC256(salt))
                .withIssuer(ISSUER)
                .build();
    }
}
