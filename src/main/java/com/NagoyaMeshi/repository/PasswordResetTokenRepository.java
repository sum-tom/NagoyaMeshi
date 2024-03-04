package com.NagoyaMeshi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUserEmail(String email);
}