package com.demo.mfa.service;

import com.demo.mfa.dto.UserDto;
import com.demo.mfa.exceprion.UserAlreadyExistException;
import com.demo.mfa.model.User;
import com.demo.mfa.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.samstevens.totp.secret.SecretGenerator;

/**
 * UserService
 */
@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretGenerator secretGenerator;

    @Override
    public User registerNewUser(UserDto userDto) throws UserAlreadyExistException {
        if (checkIfUserExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("User already exists for this email: " + userDto.getEmail());
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setSecret(secretGenerator.generate()); // Generate and add the secret to new user

        return userRepository.save(user);
    }

    private boolean checkIfUserExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
}