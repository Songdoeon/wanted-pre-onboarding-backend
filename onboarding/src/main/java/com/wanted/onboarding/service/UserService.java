package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.UserRequest;
import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.BindingException;
import com.wanted.onboarding.error.exception.UserExistException;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserRequest userRequest){

        if(userRepository.existsByUsername(userRequest.getUsername())) throw new UserExistException(CommonErrorCode.USER_EXIST);
        User userEntity = new User().builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles("ROLE_USER")
                .build();

        userRepository.save(userEntity);
    }
    public boolean bindingCheck(BindingResult bindingResult){
        if(bindingResult.hasErrors()) return false;
        return true;
    }
}
