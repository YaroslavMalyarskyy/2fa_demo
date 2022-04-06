package com.demo.mfa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserDto
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
}