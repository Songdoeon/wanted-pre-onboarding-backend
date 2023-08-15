package com.wanted.onboarding.repository;

import com.wanted.onboarding.dto.UserRequestDTO;
import com.wanted.onboarding.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    UserRepository userRepository;
    UserRequestDTO userRequestDTO;
    User userEntity;
    @BeforeEach
    void init(){
        userRequestDTO = new UserRequestDTO().builder()
                .username("song212@wanted.com")
                .password("12341234")
                .build();
        userEntity = new User().builder()
                .username(userRequestDTO.getUsername())
                .password(userRequestDTO.getPassword())
                .roles("ROLE_USER")
                .build();
    }
    @Test
    @DisplayName("아이디로 유저 찾기")
    void findByUsername() {
        em.persistAndFlush(userEntity);

        Optional<User> actual = userRepository.findByUsername(userEntity.getUsername());

        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("중복 아이디")
    void existsByUsername() {
        em.persistAndFlush(userEntity);

        boolean actual = userRepository.existsByUsername(userEntity.getUsername());

        assertThat(actual).isTrue();
    }
}