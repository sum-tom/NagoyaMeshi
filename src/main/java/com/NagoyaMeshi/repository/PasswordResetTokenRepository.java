package com.NagoyaMeshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUserEmail(String email);
}
