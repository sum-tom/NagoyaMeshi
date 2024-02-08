package com.NagoyaMeshi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Review;
public interface ReviewRepository extends JpaRepository<Review,Integer>{

}
