package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.NagoyaMeshi.entity.Reservation;
import com.NagoyaMeshi.repository.ReservationRepository;

@Controller
@RequestMapping("/admin/reservation")
public class ReservationController {
	private final ReservationRepository reservationRepository;
	
	public ReservationController( ReservationRepository reservationRepository) {
	    this.reservationRepository = reservationRepository; 
	    }	
	
	@GetMapping
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		 Page<Reservation> reservationPage = reservationRepository.findAll(pageable);       
        
        model.addAttribute("reservationPage", reservationPage);             
        
        return "/admin/reservation/reservation";
    }  
	
}
