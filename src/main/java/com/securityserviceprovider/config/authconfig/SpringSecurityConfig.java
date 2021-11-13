package com.securityserviceprovider.config.authconfig;

import com.securityserviceprovider.filter.authfilter.JwtAuthenticationFilter;
import com.securityserviceprovider.filter.authfilter.LoginAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    @Lazy
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    @Lazy
    private LoginAuthenticationFilter loginAuthenticationFilter;

    @Resource
    @Lazy
    private MyUserDetails myUserDetails;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 测试用资源，需要验证了的用户才能访问
                .antMatchers("/login/member").hasRole("MEMBER")
                .antMatchers("login/nonmember").hasAnyRole("NONMEMBER","MEMBER","MERCHANT")
                // 其他都放行了
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .csrf().disable()               //CRSF禁用，因为不使用session
                .sessionManagement().disable()      //禁用session
                .addFilter(jwtAuthenticationFilter) //添加Jwt验证的过滤器
                .addFilter(loginAuthenticationFilter)//添加Login时候的校验过滤器
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()).userDetailsService(myUserDetails).passwordEncoder(passwordEncoder());
    }

    /**
     * 将BCryptPasswordEncoder设置成bean
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBecomeBean() throws Exception {
        return authenticationManager();
    }

    @Bean
    protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
        //这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(myUserDetails);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return daoProvider;
    }

}
