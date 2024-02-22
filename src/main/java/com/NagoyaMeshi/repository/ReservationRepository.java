package com.NagoyaMeshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Reservation;
public interface ReservationRepository extends JpaRepository<Reservation,Integer>{
	public Page<Reservation> findById(String keyword, Pageable pageable);
	 Page<Reservation> findByShopIdIn(List<Integer> shopId, Pageable pageable);
}
