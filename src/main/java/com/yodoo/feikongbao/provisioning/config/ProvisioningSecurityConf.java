package com.yodoo.feikongbao.provisioning.config;

import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningAccessDeineHandler;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningAuthenticationEntryPoint;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Date 2019/7/9 17:44
 * @Author by houzhen
 */
@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProvisioningSecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    ProvisioningAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    ProvisioningAccessDeineHandler paasAccessDeineHandler;

    @Autowired
    ProvisioningAuthenticationProvider authenticationProvider;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // 加入自定义的安全认证
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()

                .httpBasic()

                .and()
                .authorizeRequests()

                .anyRequest()
                .authenticated()// 其他 url 需要身份认证
        ;

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(paasAccessDeineHandler);

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

}
