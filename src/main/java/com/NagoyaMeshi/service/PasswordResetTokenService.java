package com.NagoyaMeshi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.NagoyaMeshi.entity.PasswordResetToken;
import com.NagoyaMeshi.repository.PasswordResetTokenRepository;
import com.NagoyaMeshi.repository.UserRepository;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository, UserRepository userRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userRepository = userRepository;
    }

    //トークンの作成
    public String createPasswordResetTokenForUser(String email, String token) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // トークンの有効期限を1時間後に設定
                    PasswordResetToken myToken = new PasswordResetToken();
                    myToken.setUser(user);
                    myToken.setToken(token);
                    myToken.setExpiryDate(expiryDate);
                    passwordResetTokenRepository.save(myToken);
                    return token;
                })
                .orElse(null); // ユーザーが見つからない場合はnullを返す
    }

    
    //トークンの検証
    public String validatePasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .map(tokenEntity -> {
                    if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                        return "expired";//期限切れ
                    }
                    return "valid";//有効
                })
                .orElse("invalid");//無効
    }
}