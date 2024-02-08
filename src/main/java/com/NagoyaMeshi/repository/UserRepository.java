package com.NagoyaMeshi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.User;
public interface UserRepository extends JpaRepository<User, Integer>{

}
