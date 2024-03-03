package com.NagoyaMeshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository< VerificationToken, Integer> {
    public VerificationToken findByToken(String token);
    public VerificationToken findByUser(User user);
}
