package com.example.springcrudback.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {

    @NotBlank(message = "아이디는 비어 있을 수 없습니다.")
    private String username;

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    private String password;

}