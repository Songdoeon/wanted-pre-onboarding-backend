package com.wanted.onboarding.controllder;

import com.wanted.onboarding.dto.UserRequestDTO;
import com.wanted.onboarding.error.CommonErrorCode;
import com.wanted.onboarding.error.exception.BindingException;
import com.wanted.onboarding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String signUp(@Valid @RequestBody UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new BindingException(CommonErrorCode.BINDING_ERROR,bindingResult.getFieldError().getDefaultMessage());
        userService.signUp(userRequestDTO);
        return "회원가입완료";
    }
}
