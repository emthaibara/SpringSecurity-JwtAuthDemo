package com.securityserviceprovider.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.impl.PublicClaims.ISSUER;


/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/3
 */

@Component
public class JwtUtil {

    private static final String SALT = "1a2b3c";

    //jwt的规定，最后请求的格式应该是 `Bearer token`
    public static final String TOKENPREFIX = "Bearer ";

    public static final String TOKENHEADER = "token";

    //设置过期失效
    private static final long EXPIREDTIME = 1000L;

    public static String getUserRole(String token) {
        return JwtUtil.getTokenBody(token).verify(token).getClaim("role").asString();
    }

    //生成token
    public String generateToken(UserDetails user) {
        //SALT = BCrypt.gensalt();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUsername())    //设置主题
                .withClaim("role",getRole(user))
                .withExpiresAt(new Date(System.currentTimeMillis()+3600*EXPIREDTIME)) //过期失效
                .withIssuedAt(new Date())       //发行时间
                .sign(Algorithm.HMAC256(SALT));
    }

    public static String getSALT() {
        return SALT;
    }

    private String getRole(UserDetails user) {
        for (GrantedAuthority authority : user.getAuthorities()){
            return authority.getAuthority();
        }
        return null;
    }

    public static String getUsername(String token){
        return getTokenBody(token).verify(token).getSubject();
    }

    public static JWTVerifier getTokenBody(String token){
        return JWT.require(Algorithm.HMAC256("1a2b3c"))
                .withIssuer(ISSUER)
                .build();
    }

    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.generateToken(User.builder().
                username("13266746457").
                password(new BCryptPasswordEncoder().encode("root")).
                roles("MEMBER").
                build());
        System.out.println("token:"+token);

        //token解析
        JWTVerifier jwtVerifier = getTokenBody(token);
        System.out.println("getSubject:"+jwtVerifier.verify(token).getSubject());
        System.out.println("getPayload:"+jwtVerifier.verify(token).getClaim("role").asString());
        System.out.println("gettoken : "+jwtVerifier.verify(token).getToken());
    }

}
class JwtTest{
    public static void main(String[] args) {
        JwtUtil jwtUtil = new JwtUtil();
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMzI2Njc0NjQ1NyIsInJvbGUiOiJST0xFX01FTUJFUiIsImlzcyI6ImlzcyIsImV4cCI6MTYzNjQ3NzQyMSwiaWF0IjoxNjM2NDczODIxfQ.bZ21ROKPRoWycHdCN5K3VrjMEtKQFbTtfRmkiyUlO7c";
        System.out.println(JwtUtil.getUsername(token));

    }
}