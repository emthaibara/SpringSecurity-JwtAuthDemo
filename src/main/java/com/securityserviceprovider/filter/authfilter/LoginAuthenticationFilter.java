package com.securityserviceprovider.filter.authfilter;

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
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthenticationFilter.class);

    private final RedisUtil redisUtil;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager,RedisUtil redisUtil) {
        super(authenticationManager);
        this.redisUtil = redisUtil;
    }

    /**
        private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
                                                new AntPathRequestMatcher("/login", "POST");
            默认拦截路径/login 和 method = POST
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException{
        //我这里对body解析处理的并不是很好，如果有更好的策略可以替换
        String body ;
        body = getBody(httpServletRequest);
        log.info(body);
        String phoneNumber = null, password = null;

        if(StringUtils.hasText(body)) {
            phoneNumber = body.substring(body.indexOf("=")+1,body.indexOf("&"));
            password = body.substring(body.lastIndexOf("=")+1);
        }

        if (phoneNumber == null)
            phoneNumber = "";
        if (password == null)
            password = "";

        phoneNumber = phoneNumber.trim(); //去除前后空格，以防万一

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                phoneNumber, password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

//        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
//        log.info(userDetails.getUsername());
//        log.info(userDetails.getAuthorities().toString());

        //将生成的Auth实体Authentication存放进SecurityContextHolder用于校验------真实的做校验的对象是由UserDetails(这个可以自己重写)做对比
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     *     用流处理httpServletRequest
     *      获取body的username/password/data....
     * @param httpServletRequest httpServletRequest
     * @return return
     */
    private String getBody(HttpServletRequest httpServletRequest) {
        BufferedReader streamReader = null;
        try {
            StringBuilder body;
            streamReader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), StandardCharsets.UTF_8));
                body = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null) {
                    body.append(line);
                }
            return body.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally {
            try {
                if (!Objects.isNull(streamReader)) {
                    streamReader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
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
                                              AuthenticationException failed) throws IOException {
        log.info("[unsuccessfulAuthentication--------");
        SecurityContextHolder.clearContext();
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
