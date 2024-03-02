package com.NagoyaMeshi.controller;

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

import com.NagoyaMeshi.entity.Review;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.ReviewForm;
import com.NagoyaMeshi.repository.ReviewRepository;
import com.NagoyaMeshi.repository.ShopRepository;
import com.NagoyaMeshi.security.UserDetailsImpl;
import com.NagoyaMeshi.service.ReviewService;

@Controller
public class UserReviewController {

private final ReviewRepository reviewRepository;
private final ShopRepository shopRepository;
private final ReviewService reviewService;

	public UserReviewController(ReviewRepository reviewRepository,ReviewService reviewService,ShopRepository shopRepository) {
	    this.reviewRepository = reviewRepository; 
	    this.reviewService = reviewService;
	    this.shopRepository = shopRepository; 
	}
	
	@GetMapping("/user/review/{id}")
	public String index(@PathVariable(name = "id") Integer id,Model model, 
						@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {	
		 Page<Review> reviewPage = reviewRepository.findAll(pageable);
		 Shop shop = shopRepository.getReferenceById(id);
	        
         model.addAttribute("shop", shop);   
		 model.addAttribute("reviewPage", reviewPage);
      
      
      return "/user/review/{id}";
  }  
	

	 @GetMapping("/user/shop/{id}/review/register")
	    public String register(@PathVariable("id") Integer shopId, Model model) {
	        model.addAttribute("reviewForm", new ReviewForm());
	        model.addAttribute("shopId", shopId);
	        
	        return "/user/review/review-post" ;
	    } 
	
	
	
	
	 @PostMapping("/user/shop/{id}/review/create")
	    public String create(@PathVariable("id") Integer shopId,
	                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
	                         @ModelAttribute ReviewForm reviewForm) {

	        Shop shop = shopRepository.getReferenceById(shopId); 
	        User user = userDetailsImpl.getUser(); 

	        reviewService.create(shop, user, reviewForm);
	        return "redirect:/user/review/{id}" ;
	    }
	
	
}
