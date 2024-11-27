package com.promo.web.config;

import com.promo.web.repository.UserRepository;
import com.promo.web.security.filter.InitialAuthenticationFilter;
import com.promo.web.security.filter.JwtAuthenticationFilter;
import com.promo.web.security.handler.OAuth2FailureHandler;
import com.promo.web.security.handler.OAuth2SuccessHandler;
import com.promo.web.security.oauth.CustomOAuth2UserService;
import com.promo.web.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.promo.web.security.provider.JwtTokenProvider;
import com.promo.web.security.provider.OtpAuthenticationProvider;
import com.promo.web.security.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, InitialAuthenticationFilter initialAuthenticationFilter, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers("/api/auth/**", "/api/oauth/**").permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .anyRequest().hasAnyAuthority("USER", "ADMIN")
            );


        http
            .oauth2Login(oauth2 ->
                    oauth2
                            .authorizationEndpoint(
                                authorizationEndpointConfig
                                        -> authorizationEndpointConfig.baseUri("/api/oauth2/authorize")
                                        .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                            )
                            .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/login/oauth2/code/**"))
                            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                            .successHandler(oAuth2SuccessHandler)
                            .failureHandler(oAuth2FailureHandler)
            );


        http
            .addFilterBefore(corsFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(initialAuthenticationFilter, BasicAuthenticationFilter.class)
            .addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(OtpAuthenticationProvider otpAuthenticationProvider, UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
        return new ProviderManager(List.of(otpAuthenticationProvider, usernamePasswordAuthenticationProvider));
    }

    @Bean
    public InitialAuthenticationFilter initialAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        return new InitialAuthenticationFilter(authenticationManager, jwtTokenProvider, userRepository);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");  // Set allowed origin for client
        config.addAllowedMethod("*");                      // Allow all HTTP methods
        config.addAllowedHeader("*");                      // Allow all headers

        config.setExposedHeaders(List.of("accessToken", "refreshToken"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}