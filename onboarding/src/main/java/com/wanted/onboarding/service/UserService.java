package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.UserRequest;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserRequest userRequest){
        User userEntity = new User().builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles("ROLE_USER")
                .build();
        userRepository.save(userEntity);
    }

    public boolean signIn(UserRequest userRequest){
        User user = userRepository.findByUsername(userRequest.getUsername());
        return userRepository.existsById(user.getId());
    }

}
