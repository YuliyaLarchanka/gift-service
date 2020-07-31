package com.epam.esm.web.config;

import com.epam.esm.web.security.CustomAccessDeniedHandler;
import com.epam.esm.web.security.CustomAuthenticationEntryPoint;
import com.epam.esm.web.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/orders").hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET, "/certificates", "/certificates/*").hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET, "/tags", "/tags/*").hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET, "/accounts/*/orders").hasAnyRole("CLIENT", "ADMIN")

                .antMatchers(HttpMethod.POST,"/accounts").permitAll()
                .antMatchers("/tags", "/tags/*").hasRole("ADMIN")
                .antMatchers("/certificates", "/certificates/*").hasRole("ADMIN")
                .antMatchers("/accounts", "/accounts/*").hasRole("ADMIN")
                .antMatchers("/orders", "/orders/*").hasRole("ADMIN")

                .antMatchers("/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/certificates", "/certificates/*").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
