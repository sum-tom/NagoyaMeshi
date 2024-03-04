package com.NagoyaMeshi.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.Role;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.SignupForm;
import com.NagoyaMeshi.form.UserEditForm;
import com.NagoyaMeshi.repository.PasswordResetTokenRepository;
import com.NagoyaMeshi.repository.RoleRepository;
import com.NagoyaMeshi.repository.UserRepository;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender javaMailSender;
    
    public UserService(UserRepository userRepository, 
    					RoleRepository roleRepository, 
    					PasswordEncoder passwordEncoder,
    					PasswordResetTokenRepository passwordResetTokenRepository,
    					JavaMailSender javaMailSender) {
    	
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository =passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }    
    
    //会員登録
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setEnabled(false);      
        
        return userRepository.save(user);
    } 
    
    
    //会員情報編集
    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());
        
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail());      
        
        userRepository.save(user);
    }    
    
    
    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);  
        return user != null;
    }    
    
    // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
    
    
    // メール認証 ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true); 
        userRepository.save(user);
    } 
    
    // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());      
    }  
    
   
//    @Transactional
//    public void requestPasswordReset(String email) {
//        User user = userRepository.findByEmail(email);
//        if (user != null) {
//            // トークンの生成と有効期限の設定
//            String token = generateUniqueToken();
//            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);
//
//            // PasswordResetTokenエンティティの作成と保存
//            PasswordResetToken resetToken = new PasswordResetToken();
//            resetToken.setToken(token);
//            resetToken.setUser(user);
//            resetToken.setExpiryDate(expiryDate);
//            passwordResetTokenRepository.save(resetToken);
//
//            // メールの送信
//            sendPasswordResetEmail(user.getEmail(), token);
//        }
//    }
//    
//    
//    
//    @Transactional
//    public void resetPassword(String token, String newPassword) {
//        // トークンの検証
//        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
//        if (resetToken != null && !resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
//            // 有効期限内の場合、パスワードを更新
//            User user = resetToken.getUser();
//            user.setPassword(passwordEncoder.encode(newPassword));
//            userRepository.save(user);
//
//            // リセットトークンの削除
//            passwordResetTokenRepository.delete(resetToken);
//        } else {
//            // トークンが無効または有効期限切れの場合、適切な処理を行う
//        	 throw new RuntimeException("Invalid or expired reset token");
//        }
//    }
//
//    private String generateUniqueToken() {
//        // 実際のトークン生成ロジックに置き換える（例: UUID.randomUUID().toString()）
//        return UUID.randomUUID().toString();
//    }
//
//    private void sendPasswordResetEmail(String email, String token) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setSubject("Password Reset");
//            message.setText("Click the following link to reset your password: http://your-app-url/reset?token=" + token);
//
//            javaMailSender.send(message);
//        } catch (MailException ex) {
////             ログにエラーを記録
//            logger.error("Failed to send password reset email", ex);
//        }
//    }
    
}

