package com.hojunara.web.security.oauth;

import com.hojunara.web.entity.RegistrationMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Builder
@Getter
@Slf4j
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    String name;
    String email;
    RegistrationMethod registrationMethod;

    public static OAuthAttributes getOAuthAttribute(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        log.info("Google login oauth2 action");

        String name = (String) attributes.getOrDefault("name", "Unknown User");

        return OAuthAttributes.builder()
                .name(name)
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .registrationMethod(RegistrationMethod.Google)
                .build();
    }

    private static OAuthAttributes ofKakao(String usernameAttributeName, Map<String, Object> attributes) {
        log.info("Kakao login oauth2 action");

        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        String name = (String) profile.getOrDefault("nickname", "Unknown User");

        return OAuthAttributes.builder()
                .name(name)
                .email((String) account.get("email"))
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .registrationMethod(RegistrationMethod.KaKao)
                .build();
    }

}