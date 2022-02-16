package com.ledungcobra.configuration.security;

import com.ledungcobra.configuration.security.jwt.JwtAuthenticationFilter;
import com.ledungcobra.configuration.security.jwt.JwtUtils;
import com.ledungcobra.configuration.security.userdetails.AppUserDetails;
import com.ledungcobra.user.oauth2.CustomOAuth2User;
import com.ledungcobra.user.oauth2.CustomOAuth2UserService;
import com.ledungcobra.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomOAuth2UserService oAuth2UserService;
    private final UserService userService;
    @Value("${spring.client-url}")
    private String CLIENT_APP_URL;
    private final JwtUtils jwtUtils;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, CustomOAuth2UserService oAuth2UserService, UserService userService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.oAuth2UserService = oAuth2UserService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .mvcMatchers("/api/**", "/auth/**")
                .anyRequest()
                .permitAll()
                .and().csrf().disable()
                .cors().disable()
        ;

        http.
                oauth2Login()
                .authorizationEndpoint()
                .baseUri("/users/login")
                .and()
//                .authorizationRequestRepository(authorizationRequestRepository())
//                .and()
                .redirectionEndpoint()
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and().successHandler((req, res, authentication) -> {
                    try {
                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                        var user = userService.processOAuthPostLogin(oauthUser.getEmail(), oauthUser);

                        log.info("Authorization for " + oauthUser.getEmail());
                        var token = jwtUtils.generateToken(new AppUserDetails(user));
                        res.sendRedirect(String.format("%s/login?token=%s&email=%s&username=%s&fullname=%s", CLIENT_APP_URL, token, user.getEmail(), user.getUserName(), user.getNormalizedUserName()));
                    } catch (Exception e) {
                        log.error("Authorization error {}", e.getMessage());
                    }
                });
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
