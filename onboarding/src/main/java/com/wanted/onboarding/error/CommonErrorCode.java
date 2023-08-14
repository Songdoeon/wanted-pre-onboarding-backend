package com.wanted.onboarding.error;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    BINDING_ERROR(HttpStatus.BAD_REQUEST),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "User Not Found Error"),
    NOT_FOUND_ARTICLE(HttpStatus.BAD_REQUEST, "Article Not Found Error"),
    NOT_MATCHED_WRITER(HttpStatus.BAD_REQUEST, "Not Matched Writer"),
    USER_EXIST(HttpStatus.BAD_REQUEST, "User Already Exist");

    private final HttpStatus httpStatus;
    private String message;
    }
