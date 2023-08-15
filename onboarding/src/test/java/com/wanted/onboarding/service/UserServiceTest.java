package com.wanted.onboarding.service;

import com.wanted.onboarding.dto.UserRequestDTO;
import com.wanted.onboarding.error.exception.UserExistException;
import com.wanted.onboarding.model.User;
import com.wanted.onboarding.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;
    UserRequestDTO userRequestDTO;
    User userEntity;
    @BeforeEach
    void init(){
        userRequestDTO = new UserRequestDTO().builder()
                .username("song212@wanted.com")
                .password(passwordEncoder.encode("12341234"))
                .build();
        userEntity = new User().builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .roles("ROLE_USER")
                .build();

    }
    @Test
    @DisplayName("정상적인 회원가입")
    void signUp() {
        when(userRepository.save(any())).thenReturn(userEntity);

        String expect = userService.signUp(userRequestDTO);

        assertThat(expect).isEqualTo(userRequestDTO.getUsername());
    }

    @Test
    @DisplayName("이미 존재하는 아이디")
    void AlreadyExistUsername() {
        when(userRepository.existsByUsername(any())).thenReturn(true);
        UserExistException exception = assertThrows(UserExistException.class,
                () -> userService.signUp(userRequestDTO));

        String message = exception.getErrorCode().getMessage();
        assertThat(message).isEqualTo("User Already Exist");
    }
}