package com.demo.mfa.service;

import com.demo.mfa.dto.UserDto;
import com.demo.mfa.model.User;

/**
 * IUserService
 */
public interface IUserService {

    User registerNewUser(UserDto userDto);
}