package com.securityserviceprovider.filter.authfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securityserviceprovider.exception.BaseException;
import com.securityserviceprovider.pojo.LoginVo;
import com.securityserviceprovider.util.JwtUtil;
import com.securityserviceprovider.util.RedisKeyPrefix;
import com.securityserviceprovider.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthenticationFilter.class);

    private final RedisUtil redisUtil;

    private final ObjectMapper objectMapper;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager, RedisUtil redisUtil, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
    }


    /**
        private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
                                                new AntPathRequestMatcher("/login", "POST");
            默认拦截路径/login 和 method = POST
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws BaseException{
        //我这里对body解析处理的并不是很好，如果有更好的策略可以替换
        String phoneNumber, password;
        LoginVo loginVo;
        try {
            loginVo = objectMapper.readValue(httpServletRequest.getInputStream(), LoginVo.class);
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new BaseException(e.getMessage());
        }
        if (!Objects.isNull(loginVo)){
            password = loginVo.getPassword();
            phoneNumber = loginVo.getPhoneNumber();
        }else {
            throw new BaseException("登陆表单信息填写不完整");
        }
        phoneNumber = phoneNumber.trim(); //去除前后空格，以防万一

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                phoneNumber, password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

        //将生成的Auth实体Authentication存放进SecurityContextHolder用于校验------真实的做校验的对象是由UserDetails(这个可以自己重写)做对比
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        //一个用户一个登陆，如果当前用户已经被登陆过，那么就把另外一个登陆此用户的token令牌删除
        if (isOnline(userDetails.getUsername())){
            deleteCacheToken(userDetails.getUsername());
        }

        String token = JwtUtil.generateToken(userDetails);
        cacheToken(userDetails.getUsername(),token);

        log.info("[successfulAuthentication--------token----:]{}",token);
        response.setHeader("token", JwtUtil.TOKENPREFIX + token);
        chain.doFilter(request,response);
    }

    private void deleteCacheToken(String phoneNumber) {
        String cacheToken = redisUtil.get(RedisKeyPrefix.IDPREFIX+phoneNumber);
        redisUtil.delete(RedisKeyPrefix.TOKENPREFIX+cacheToken);
    }

    private Boolean isOnline(String phoneNumber){
        Boolean isOnline = redisUtil.hasKey(RedisKeyPrefix.IDPREFIX+phoneNumber);
        return isOnline != null && isOnline;    //<==>isOnline == null ? false : isOnline;
    }

    private void cacheToken(String phoneNumber,String token){
        redisUtil.set(RedisKeyPrefix.TOKENPREFIX +token,JwtUtil.getSalt());
        redisUtil.set(RedisKeyPrefix.IDPREFIX+phoneNumber,token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        log.info("[unsuccessfulAuthentication--------");
        SecurityContextHolder.clearContext();
        throw new BaseException("authentication failed, reason: " + failed.getMessage());
    }
}
