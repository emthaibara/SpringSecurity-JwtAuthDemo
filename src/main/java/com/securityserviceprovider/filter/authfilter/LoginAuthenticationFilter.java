package com.securityserviceprovider.filter.authfilter;

import com.securityserviceprovider.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginAuthenticationFilter.class);


    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
        private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
                                                new AntPathRequestMatcher("/login", "POST");
            默认拦截路径/login 和 method = POST
     */

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException{
        String body ;
        body = getBody(httpServletRequest);
        log.info(body);
        String phoneNumber = null, password = null;
        if(StringUtils.hasText(body)) {
            phoneNumber = body.substring(body.indexOf("=")+1,body.indexOf("&"));
            password = body.substring(body.lastIndexOf("=")+1);
            System.out.println("phoneNumber="+phoneNumber+" password="+password);
        }
        if (phoneNumber == null)
            phoneNumber = "";
        if (password == null)
            password = "";
        phoneNumber = phoneNumber.trim(); //去除前后空格，以防万一
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                phoneNumber, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     *     用流处理httpServletRequest
     *      获取body的username/password/data....
     * @param httpServletRequest httpServletRequest
     * @return return
     */
    private String getBody(HttpServletRequest httpServletRequest) {
        try {
            StringBuilder body;
            try (BufferedReader streamReader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), StandardCharsets.UTF_8))) {
                body = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null) {
                    body.append(line);
                }
            }
            return body.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = new JwtUtil().generateToken((UserDetails) authResult.getPrincipal());
        log.info("[successfulAuthentication--------token----:]{}",token);
        response.setHeader("token", JwtUtil.TOKENPREFIX + token);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("[unsuccessfulAuthentication--------");
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}