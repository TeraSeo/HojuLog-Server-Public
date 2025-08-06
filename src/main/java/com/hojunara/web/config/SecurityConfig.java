package com.hojunara.web.config;

import com.hojunara.web.repository.UserRepository;
import com.hojunara.web.security.filter.InitialAuthenticationFilter;
import com.hojunara.web.security.filter.JwtAuthenticationFilter;
import com.hojunara.web.security.handler.OAuth2FailureHandler;
import com.hojunara.web.security.handler.OAuth2SuccessHandler;
import com.hojunara.web.security.oauth.CustomOAuth2UserService;
import com.hojunara.web.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.security.provider.OtpAuthenticationProvider;
import com.hojunara.web.security.provider.UsernamePasswordAuthenticationProvider;
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

/**
 * Spring Security configuration for the application.
 * <p>
 * This configuration sets up security filters, CORS policy, OAuth2 login,
 * JWT authentication, and OTP-based login using custom filters and providers.
 * </p>
 * Provides beans for:
 * <ul>
 *     <li>{@link SecurityFilterChain}</li>
 *     <li>{@link BCryptPasswordEncoder}</li>
 *     <li>{@link AuthenticationManager}</li>
 *     <li>{@link InitialAuthenticationFilter}</li>
 *     <li>{@link JwtAuthenticationFilter}</li>
 *     <li>{@link CorsFilter}</li>
 *     <li>{@link HttpCookieOAuth2AuthorizationRequestRepository}</li>
 * </ul>
 *
 * @author Taejun Seo
 */
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

    /**
     * Defines the security filter chain with CORS, CSRF, OAuth2 login, and JWT filters.
     *
     * @param http the {@link HttpSecurity} configuration
     * @param initialAuthenticationFilter filter for username/password + OTP login
     * @param jwtAuthenticationFilter filter for validating JWT tokens
     * @return the configured {@link SecurityFilterChain}
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, InitialAuthenticationFilter initialAuthenticationFilter, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers("login", "/api/auth/**", "/api/oauth/**", "/api/post/get/**", "/api/user/get/**", "/api/comment/get/**", "/api/post/add/victory").permitAll()
                            .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                            .anyRequest().hasAnyAuthority("USER", "ADMIN")
            );


        http
            .oauth2Login(oauth2 ->
                    oauth2
                            .authorizationEndpoint(
                                authorizationEndpointConfig
                                        -> authorizationEndpointConfig.baseUri("/oauth2/authorize")
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

    /**
     * Provides an {@link AuthenticationManager} configured with both OTP and username-password providers.
     *
     * @param otpAuthenticationProvider the custom OTP-based provider
     * @param usernamePasswordAuthenticationProvider the username-password-based provider
     * @return the configured {@link AuthenticationManager}
     */
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
        return new JwtAuthenticationFilter(jwtTokenProvider, userRepository);
    }

    /**
     * Configures and returns a CORS filter allowing specific origins and headers.
     *
     * @return the configured {@link CorsFilter}
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://3.24.221.82");  // Set allowed origin for client
        config.addAllowedOrigin("http://hojulog.com");
        config.addAllowedOrigin("https://hojulog.com");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedMethod("*");                      // Allow all HTTP methods
        config.addAllowedHeader("*");                      // Allow all headers
        config.addAllowedOriginPattern("*");

        config.setExposedHeaders(List.of("userId", "accessToken", "refreshToken"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}