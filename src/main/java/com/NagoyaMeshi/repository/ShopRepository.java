package com.NagoyaMeshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Shop;
public interface ShopRepository extends JpaRepository<Shop,Integer>{
	public Page<Shop> findByNameLike(String keyword, Pageable pageable);
	public List<Shop> findByNameLike(String keyword);
	public Page<Shop> findByCategoryId(Integer categoryId, Pageable pageable);
	
	public Page<Shop> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Shop> findAllByOrderByPriceLowerLimitAsc(Pageable pageable);  

}
