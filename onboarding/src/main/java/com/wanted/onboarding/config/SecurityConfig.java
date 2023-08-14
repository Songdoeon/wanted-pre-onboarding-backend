package com.wanted.onboarding.config;


import com.wanted.onboarding.auth.PrincipalDetailsService;
import com.wanted.onboarding.filter.JwtAuthenticationFilter;
import com.wanted.onboarding.filter.JwtAuthorizationFilter;
import com.wanted.onboarding.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        return http.authorizeRequests(authroize -> authroize.antMatchers("/api/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll())
            .authenticationManager(authenticationManager)
            .addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager,userRepository))
            .formLogin()
                .disable()
            .csrf()
                .disable()
            .httpBasic()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
