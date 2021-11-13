package com.securityserviceprovider.filter.authfilter;
import com.securityserviceprovider.util.JwtUtil;
import com.securityserviceprovider.util.RedisKeyPrefix;
import com.securityserviceprovider.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/8
 */
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Resource
    private RedisUtil redisUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //在这里进行检验token
        String tokenHeader = request.getHeader(JwtUtil.TOKENHEADER);
        // 如果请求头中没有token信息则直接放行了
        if (tokenHeader == null || !tokenHeader.startsWith(JwtUtil.TOKENPREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        log.info("tokenHeader:"+tokenHeader);
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtUtil.TOKENPREFIX, "");
        String salt = redisUtil.get(RedisKeyPrefix.TOKENPREFIX +token);
        String username = JwtUtil.getUsername(token, salt);
        if (username != null){
            String role = JwtUtil.getUserRole(token , salt);
            log.info("username:"+username+" role:"+role);
            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }

    private static final int tokenRefreshInterval = 300;

    /**
     *          如果jwt验证成功，首先检查token的发放时间是否超过5分钟
     *          如果超时则刷新token，并返回新的token，刷新redis和header中的token
     *              在这之前，需要去缓存中拿到salt
     * @param request request
     * @param response  response
     * @param authResult authResult
     * @throws IOException IOException
     */
    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        //刷新token的时间

    }

    protected Boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        //验证失败直接响应动态码
        SecurityContextHolder.clearContext();
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
