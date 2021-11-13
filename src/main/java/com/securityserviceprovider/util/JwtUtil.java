package com.securityserviceprovider.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private static String salt = null;

    private static final long EXPIREDTIME = 1728000000L;

    //jwt的规定，最后请求的格式应该是 `Bearer token`
    public static final String TOKENPREFIX = "Bearer ";

    public static final String TOKENHEADER = "token";

    //设置过期失效


    public JwtUtil() {

    }

    public static String getUserRole(String token,String salt) {
        JWTVerifier jwtVerifier = getJWTVerifier(salt);
        if (jwtVerifier != null){
            return jwtVerifier.verify(token).getClaim("role").asString();
        }else {
            return null;
        }
    }

    //生成token
    public static String generateToken(UserDetails user) {
        salt = BCrypt.gensalt();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getUsername())    //设置主题
                .withClaim("role",getRole(user))
                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIREDTIME)) //过期失效
                .withIssuedAt(new Date())       //发行时间
                .sign(Algorithm.HMAC256(salt));
    }

    private static String getRole(UserDetails user) {
        for (GrantedAuthority authority : user.getAuthorities()){
            return authority.getAuthority();
        }
        return null;
    }

    public static String getUsername(String token,String salt){
        JWTVerifier jwtVerifier = getJWTVerifier(salt);
        if (jwtVerifier != null){
            return jwtVerifier.verify(token).getSubject();
        }else {
            return null;
        }
    }

    private static JWTVerifier getJWTVerifier(String salt){
        try {
            return JWT.require(Algorithm.HMAC256(salt))
                    .withIssuer(ISSUER)
                    .build();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }


    public static String getSalt() {
        return salt;
    }
}
