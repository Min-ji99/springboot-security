package com.hospital.review.service;

import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exception.ErrorCode;
import com.hospital.review.exception.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import com.hospital.review.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60; // 1시간
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

    public String login(String userName, String password) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new HospitalReviewAppException(ErrorCode.NOT_FOUND, String.format("%s는 존재하지 않습니다.", userName)));

        if(encoder.matches(password, user.getPassword())){
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD, String.format("userName 또는 password가 잘못 되었습니다."));
        }
        return JwtTokenUtil.createToken(userName, secretKey, expireTimeMs);
    }
}
