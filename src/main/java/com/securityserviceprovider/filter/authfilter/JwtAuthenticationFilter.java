package com.securityserviceprovider.filter.authfilter;
import com.securityserviceprovider.util.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/8
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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
        String username = JwtUtil.getUsername(token);
        String role = JwtUtil.getUserRole(token);
        if (username != null){
            return new UsernamePasswordAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );
        }
        return null;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
    }

    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        //验证失败直接响应动态码
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
