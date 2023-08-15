package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.UserRequestDTO;
import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.UserExistException;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String signUp(UserRequestDTO userRequestDTO){

        if(userRepository.existsByUsername(userRequestDTO.getUsername())) throw new UserExistException(CommonErrorCode.USER_EXIST);
        User userEntity = new User().builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .roles("ROLE_USER")
                .build();

        userRepository.save(userEntity);

        return userEntity.getUsername();
    }
}
