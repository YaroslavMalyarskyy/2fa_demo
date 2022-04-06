package com.demo.mfa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 72, nullable = false)
    private String password;

    @Setter(AccessLevel.NONE)
    @Column(name = "is_mfa_enabled", length = 72, nullable = false)
    private boolean isMfaEnabled = true; // flag to indicate of mfa is active for profile is always true

    @Column(name = "secret", length = 72, nullable = false)
    private String secret; // secret store for the profile, this will be used during the login process.
}