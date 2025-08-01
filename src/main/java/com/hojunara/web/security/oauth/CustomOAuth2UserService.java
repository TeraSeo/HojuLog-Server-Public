package com.hojunara.web.security.oauth;

import com.hojunara.web.entity.Role;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.UserAlreadyExistsException;
import com.hojunara.web.exception.UserLockedException;
import com.hojunara.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        log.info("oauth user loaded");

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.getOAuthAttribute(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Optional<User> u = userRepository.findByEmail(oAuthAttributes.getEmail());
        if (u.isEmpty()) {
            String randomPassword = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(randomPassword);

            User user = User.builder().username(oAuthAttributes.getName()).email(oAuthAttributes.getEmail()).password(encodedPassword).registrationMethod(oAuthAttributes.getRegistrationMethod()).log(100L).likeCountThisWeek(0L).build();
            userRepository.save(user);

            log.info("oauth user loading succeeded");
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(Role.USER.toString())),
                    oAuthAttributes.getAttributes(),
                    oAuthAttributes.getNameAttributeKey()
            );
        }
        else {
            User user = u.get();
            if (oAuthAttributes.registrationMethod.equals(user.getRegistrationMethod())) {
                if (user.getIsLocked()) {
                    throw new UserLockedException("User is locked with email: " + oAuthAttributes.getEmail());
                }

                log.info("oauth user loading succeeded");
                return new DefaultOAuth2User(
                        Collections.singleton(new SimpleGrantedAuthority(u.get().getRole().toString())),
                        oAuthAttributes.getAttributes(),
                        oAuthAttributes.getNameAttributeKey()
                );
            }
            throw new UserAlreadyExistsException("User already exists with email: " + oAuthAttributes.getEmail());
        }
    }
}