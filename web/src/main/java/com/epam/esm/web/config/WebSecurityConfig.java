package com.epam.esm.web.config;

import com.epam.esm.web.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers(HttpMethod.POST,"/accounts").permitAll()
                .antMatchers("/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/certificates", "/certificates/*").permitAll()
                .antMatchers(HttpMethod.GET, "/tags", "/tags/*").permitAll()

                .antMatchers(HttpMethod.POST,"/orders").hasAnyRole("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/accounts/my-orders").hasAnyRole("CLIENT", "ADMIN")

                .antMatchers("/accounts/**").hasRole("ADMIN")
                .antMatchers("/tags/**").hasRole("ADMIN")
                .antMatchers("/certificates/**").hasRole("ADMIN")
                .antMatchers("/orders/**").hasRole("ADMIN")

                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
