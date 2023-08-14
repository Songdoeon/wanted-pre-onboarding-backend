package com.wanted.onboarding.error.exception;

import com.wanted.onboarding.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotMatchedWriterException extends RuntimeException{
    final ErrorCode errorCode;
}
