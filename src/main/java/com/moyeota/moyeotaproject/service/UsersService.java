package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.SignUpRequestDto;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.domain.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignUpRequestDto signUpRequestDto) {
        Optional<Users> user = usersRepository.findByLoginId(signUpRequestDto.getLoginId());
        if (user.isPresent()) {
            throw new RuntimeException("로그인 ID 중복입니다");
        } else {
            Users createUser = Users.builder()
                    .loginId(signUpRequestDto.getLoginId())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .name(signUpRequestDto.getName())
                    .profileImage(signUpRequestDto.getProfileImage())
                    .phoneNumber(signUpRequestDto.getPhoneNumber())
                    .email(signUpRequestDto.getEmail())
                    .status(signUpRequestDto.getStatus())
                    .gender(signUpRequestDto.getGender())
                    .averageStarRate(signUpRequestDto.getAverageStarRate())
                    .school(signUpRequestDto.getSchool())
                    .isAuthenticated(false)
                    .build();
            usersRepository.save(createUser);
        }
    }


}
