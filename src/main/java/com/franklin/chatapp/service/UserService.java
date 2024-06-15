package com.franklin.chatapp.service;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.franklin.chatapp.entity.User;
import com.franklin.chatapp.entity.User.Role;
import com.franklin.chatapp.exception.InvalidInputException;
import com.franklin.chatapp.exception.InvalidUsernameException;
import com.franklin.chatapp.repository.UserRepository;
import com.franklin.chatapp.security.CustomOAuth2User;
import com.franklin.chatapp.util.CustomValidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final CustomValidator<User> customValidator = new CustomValidator<>();

    public User currentUser(CustomOAuth2User oAuth2User) {
        return oAuth2User.getUser();
    }

    public void newUser(User user, String username, OAuth2User oAuth2User) {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidUsernameException(username + " is already taken. Try a different username.", username);
        }
        user.setUsername(username);
        user.setRole(Role.USER);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new InvalidUsernameException(new ConstraintViolationException(violations).getMessage(), username);
        }
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) oAuth2User;
        customOAuth2User.refreshAuthority();
        Authentication newAuth = new OAuth2AuthenticationToken(customOAuth2User, customOAuth2User.getAuthorities(),
                customOAuth2User.getName());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findUserJoinedWithGroupChat(String username) {
        return userRepository.findUserJoinedWithGroupChat(username)
                .orElseThrow(() -> new InvalidInputException(username + " is invalid."));
    }

    public User updateDisplayName(User user, String displayName) {
        user.setDisplayName(displayName);
        customValidator.validate(user);
        return userRepository.save(user);
    }

    public User getUserFromWebSocket(Principal principal) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        return oAuth2User.getUser();
    }
}