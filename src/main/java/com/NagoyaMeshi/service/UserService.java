package com.NagoyaMeshi.service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.PasswordResetToken;
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
        Optional<User> user = userRepository.findByEmail(email);  
        return user.isPresent();
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
    
    
    
    // パスワードリセットトークンの生成とメール送信
    @Transactional
    public boolean createPasswordResetTokenForUserAndSendEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);  
        if (!userOptional.isPresent()) {
            return false; // ユーザーが見つからない場合は、falseを返す
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();//ランダムなUUIDをトークンとして生成
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24); // 24時間後に有効期限が切れる

        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(expiryDate);

        passwordResetTokenRepository.save(myToken);

        sendPasswordResetEmail(user.getEmail(), token);
        return true; // 処理が成功した場合はtrueを返す
    }

    // パスワードリセットメールの送信
    private void sendPasswordResetEmail(String email, String token) {
        String subject = "パスワード再設定リンク";
        String passwordResetUrl = "http://localhost:8080/password-reset/reset?token=" + token;
        String message = "パスワードを再設定するには、以下のリンクをクリックしてください: " + passwordResetUrl;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("tomatomatover@gmail.com");
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        javaMailSender.send(emailMessage);
    }
    
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        return passwordResetTokenRepository.findByToken(token)
                .map(tokenEntity -> {
                    User user = tokenEntity.getUser();
                    if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                        return false; // トークンの有効期限が切れている場合
                    }
                    String encodedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encodedPassword);
                    userRepository.save(user);
                    passwordResetTokenRepository.delete(tokenEntity); // トークンを使用済みとして削除
                    return true;
                })
                .orElse(false); // トークンが見つからない場合
    }
    
}

