package com.obs.config;

import com.obs.security.CustomSavedRequestAwareAuthenticationSuccessHandler;
import com.obs.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Created by ongbo on 2/2/2017.
 */
@Configuration
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomSavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSavedRequestAwareAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        auth.inMemoryAuthentication()
                .withUser("admin").password("P@ssw0rd").roles("ADMIN").and()
                .withUser("user").password("P@ssw0rd").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/ping").authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout();
    }
}
