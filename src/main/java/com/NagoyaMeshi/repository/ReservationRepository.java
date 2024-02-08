package com.NagoyaMeshi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Reservation;
public interface ReservationRepository extends JpaRepository<Reservation,Integer>{

}
