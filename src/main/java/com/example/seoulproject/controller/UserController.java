package com.example.seoulproject.controller;

import com.example.seoulproject.authentication.AuthenticatedUser;
import com.example.seoulproject.dto.response.ResponseDto;
import com.example.seoulproject.dto.response.user.UserResponseDto;
import com.example.seoulproject.entity.User;
import com.example.seoulproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUserInfo(@AuthenticatedUser User user) {
        UserResponseDto userResponseDto = userService.getUserInfo(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 정보 조회 완료", userResponseDto), HttpStatus.OK);
    }
}
