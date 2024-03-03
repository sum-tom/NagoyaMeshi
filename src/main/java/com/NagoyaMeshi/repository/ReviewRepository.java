package com.NagoyaMeshi.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Review;
import com.NagoyaMeshi.entity.Shop;
public interface ReviewRepository extends JpaRepository<Review,Integer>{
	Page<Review> findByShop(Shop shop, Pageable pageable);
}
