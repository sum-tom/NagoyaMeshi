package com.NagoyaMeshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.Reservation;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.ReservationRegisterForm;
import com.NagoyaMeshi.repository.ReservationRepository;
import com.NagoyaMeshi.repository.ShopRepository;
import com.NagoyaMeshi.repository.UserRepository;

@Service
public class ReservationService {
	 private final ReservationRepository reservationRepository;  
     private final ShopRepository shopRepository;  
     private final UserRepository userRepository;  

     public ReservationService(ReservationRepository reservationRepository, ShopRepository shopRepository, UserRepository userRepository) {
         this.reservationRepository = reservationRepository;  
         this.shopRepository = shopRepository;  
         this.userRepository = userRepository;  
     }    

     @Transactional
     public void create(Shop shop, User user,ReservationRegisterForm reservationRegisterForm) { 
         Reservation reservation = new Reservation();
        
         reservation.setShop(shop);
         reservation.setUser(user);
         reservation.setReservationTime(reservationRegisterForm.getReservationTime());
         reservation.setReservationDate(reservationRegisterForm.getReservationDate());
         reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
         
         
         reservationRepository.save(reservation);
     }    
     

}
