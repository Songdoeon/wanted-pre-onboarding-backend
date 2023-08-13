package com.wanted.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.persistence.Column;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Pattern(regexp = ".*@.*", message = "아이디는 이메일 형식이어야합니다.")
    private String username;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야합니다.")
    private String password;
}
