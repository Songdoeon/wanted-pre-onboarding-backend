package com.wanted.onboarding.controllder;

import com.wanted.onboarding.dto.UserRequest;
import com.wanted.onboarding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("sign-up")
    public String signUp(@RequestBody UserRequest userRequest) {
        userService.signUp(userRequest);
        return "회원가입완료";
    }
}
