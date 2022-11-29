package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserDto join(UserJoinRequest request){
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, "");
                });
        User user=userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }
}
