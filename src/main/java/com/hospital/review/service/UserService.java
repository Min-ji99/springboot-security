package com.hospital.review.service;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;
import com.hospital.review.HospitalReviewApplication;
import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto join(UserJoinRequest request){
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{
                    throw new HospitalReviewAppException(ErrorCode.DULICATED_USER_NAME, String.format("Username: %s", request.getUserName()));
                });
        User user=userRepository.save(request.toEntity());
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }
}
