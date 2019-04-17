package com.example.sweater.config;

import com.example.sweater.service.UserServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурация веб безопасности
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userService;

    private final PasswordEncoder passwordEncoder;

    private final Logger log;

    @Autowired
    public WebSecurityConfig(final UserServiceImpl userService, final PasswordEncoder passwordEncoder, final Logger log) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.log = log;
    }

    @Override
    protected void configure(final HttpSecurity http) {
        try {
            http.authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout()
                    .permitAll();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        try {
            auth.userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
