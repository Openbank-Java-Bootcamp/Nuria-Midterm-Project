package com.ironhack.midtermproject.security;

import com.ironhack.midtermproject.filter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/checking/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/checking/{id}/balance/**").hasAnyAuthority("ADMIN", "HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/checking/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/checking/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/checking/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/checking/**").hasAnyAuthority("ADMIN", "HOLDER");

        http.authorizeRequests().antMatchers(POST, "/api/credit/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/credit/{id}/balance/**").hasAnyAuthority("ADMIN", "HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/credit/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/credit/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/credit/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/credit/**").hasAnyAuthority("ADMIN", "HOLDER");

        http.authorizeRequests().antMatchers(POST, "/api/savings/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/savings/{id}/balance/**").hasAnyAuthority("ADMIN", "HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/savings/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/savings/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/savings/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/savings/**").hasAnyAuthority("ADMIN", "HOLDER");

        http.authorizeRequests().antMatchers(POST, "/api/student/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/student/{id}/balance/**").hasAnyAuthority("ADMIN", "HOLDER");
        http.authorizeRequests().antMatchers(GET, "/api/student/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/student/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/student/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/student/**").hasAnyAuthority("ADMIN", "HOLDER");

        http.authorizeRequests().antMatchers(POST, "/api/holder/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/holder/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/holder/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/holder/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/admin/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/roles/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/third/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/third/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/third/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/third/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PATCH, "/api/third/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/users/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
