package com.hojunara.web.security.handler;

import com.hojunara.web.exception.UserAlreadyExistsException;
import com.hojunara.web.exception.UserLockedException;
import com.hojunara.web.security.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = "https://hojulog.com/register/failed";
        if (exception instanceof UserAlreadyExistsException) {
            targetUrl = UriComponentsBuilder.fromHttpUrl(targetUrl)
                    .queryParam("error", "존재하는 계정이 있습니다")
                    .toUriString();
        }
        else if (exception instanceof UserLockedException) {
            targetUrl = UriComponentsBuilder.fromHttpUrl(targetUrl)
                    .queryParam("error", "비활성화된 계정입니다")
                    .toUriString();
        }

        removeAuthorizationRequestCookies(request, response);

        log.info("authentication failed and redirect");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        log.info("remove authorization request cookies");
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}