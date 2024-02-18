package com.NagoyaMeshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.repository.ReservationRepository;

@Controller
@RequestMapping("/admin/reservation")
public class ReservationController {
	private final ReservationRepository reservationRepository;
	
	public ReservationController( ReservationRepository reservationRepository) {
	    this.reservationRepository = reservationRepository; 
	    }	
	
//	@GetMapping
//	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
//		 Page<Reservation> reservationPage; 
//        
//		 if (keyword != null && !keyword.isEmpty()) {
//			 reservationPage = reservationRepository.findByNameLike("%" + keyword + "%", pageable);                
//         } else {
//        	 reservationPage = reservationRepository.findAll(pageable);
//         }  
//		 
//        model.addAttribute("reservationPage", reservationPage);             
//        model.addAttribute("keyword", keyword);
//        
//        return "/admin/reservation/reservation";
//    }  
	
	@PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
		reservationRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "予約を削除しました。");
        
        return "redirect:/admin/reservation";
    }
	
}
