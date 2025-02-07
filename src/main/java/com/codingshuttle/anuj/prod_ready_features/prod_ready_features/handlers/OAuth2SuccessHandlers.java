package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.handlers;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.JwtServices;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandlers extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtServices jwtServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        User user = userService.getUserByEmail(email);

        if(user == null){
            User newUser = User.builder()
                    .email(email)
                    .name(oAuth2User.getAttribute("name"))
                    .build();
            user = userService.save(newUser);
        }

        String accessToken = jwtServices.generateAccessToken(user);
        String refreshToken = jwtServices.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
    }
}
