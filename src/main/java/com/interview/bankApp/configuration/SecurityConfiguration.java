package com.interview.bankApp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}adminPassword").roles("USER", "ADMIN");

    }

    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/accounts/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/transactions/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/account").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/accounts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/transactions/**").hasRole("ADMIN")
                .and()
                .csrf().disable();
    }
}