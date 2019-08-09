package com.atguigu.gulimall.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SmsSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        /**
         * 所有http请求都让他通过  要不然就授权登录
         */
        http.authorizeRequests().antMatchers("/**").permitAll();

        http.csrf().disable();

    }
}
