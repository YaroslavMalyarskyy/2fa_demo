package com.demo.mfa.repository;

import com.demo.mfa.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}