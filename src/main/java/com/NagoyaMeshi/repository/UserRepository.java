package com.NagoyaMeshi.repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    Page<User> findByNameLike(String keyword, Pageable pageable);
    Optional<User> findByEmail(String email);
}
