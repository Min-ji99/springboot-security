package com.hospital.review.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DULICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated.");

    private HttpStatus httpStatus;
    private String message;
}
