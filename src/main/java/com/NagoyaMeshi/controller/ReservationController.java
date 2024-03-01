package com.NagoyaMeshi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.Reservation;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.ReservationRegisterForm;
import com.NagoyaMeshi.repository.ReservationRepository;
import com.NagoyaMeshi.repository.ShopRepository;
import com.NagoyaMeshi.security.UserDetailsImpl;
import com.NagoyaMeshi.service.ReservationService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService; 
	private final ShopRepository shopRepository;
	
	public ReservationController( ReservationRepository reservationRepository,ReservationService reservationService,ShopRepository shopRepository) {
	    this.reservationRepository = reservationRepository; 
	    this.reservationService = reservationService;
	    this.shopRepository = shopRepository;
	    }	
	
	//Admin
	@GetMapping("/admin/reservation")
    public String index(Model model,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        @RequestParam(name = "keyword", required = false) String keyword) {
        List<Shop> shopList;
        Page<Reservation> reservationPage;

        if (keyword != null && !keyword.isEmpty()) {
            // Step 1: Search for shops by name
            shopList = shopRepository.findByNameLike("%" + keyword + "%");

            // Step 2: Retrieve shop IDs and store them in a list
            List<Integer> shopId = shopList.stream()
                    .map(Shop::getId)
                    .collect(Collectors.toList());

            // Step 3: Search for reservations based on shop IDs
            reservationPage = reservationRepository.findByShopIdIn(shopId, pageable);
        } else {
            // If no keyword provided, fetch all reservations
            reservationPage = reservationRepository.findAll(pageable);
        }

        model.addAttribute("reservationPage", reservationPage);
        model.addAttribute("keyword", keyword);

        return "/admin/reservation/reservation";
    }

    @PostMapping("/admin/reservation/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        reservationRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("successMessage", "予約を削除しました。");

        return "redirect:/admin/reservation";
    }
    
    
    //User
    @GetMapping("/user/shop/{id}/reservation/register")
    public String register(@PathVariable("id") Integer shopId,Model model) {
        model.addAttribute("ReservationRegisterForm", new ReservationRegisterForm());
        model.addAttribute("shopId", shopId);
        
        return "/user/shop/shop-reservation";
    } 
    
    @PostMapping("/user/shop/{id}/reservation/create")
	 public String create(@PathVariable("id") Integer shopId,
             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
             @ModelAttribute ReservationRegisterForm reservationRegisterForm) {

    	Shop shop = shopRepository.getReferenceById(shopId); 
    	User user = userDetailsImpl.getUser(); 
	 
    	reservationService.create(shop, user, reservationRegisterForm);
    	return "redirect:/user/reservation";
    }    
    
    @GetMapping("/user/reservation")
    public String index(Model model, Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){		
	Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
	
		model.addAttribute("reservationPage", reservationPage); 

        return "/user/shop/shop-reservation-list";
    }
    
    @PostMapping("/user/reservation/{id}/delete")
    public String userdelete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        reservationRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("successMessage", "予約を削除しました。");

        return "redirect:/user/reservation";
    }
    
}
