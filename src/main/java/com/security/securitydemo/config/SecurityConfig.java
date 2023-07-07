package com.security.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .authorizeHttpRequests(authz->authz
                    .requestMatchers("v1/notSecured").permitAll()
                    .anyRequest().authenticated())
                .formLogin(formConfig->formConfig
                    .successHandler(successHandler())
                    .permitAll())
                .sessionManagement(sessionManagementCustomizer->sessionManagementCustomizer
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry())) //ALWAYS, IF_REQUIRED, NEVER, STATELESS
                .build();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request,response,authentication)->{
            response.sendRedirect("/v1/session");
        });
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}
