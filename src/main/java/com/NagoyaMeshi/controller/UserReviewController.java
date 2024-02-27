package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.NagoyaMeshi.entity.Review;
import com.NagoyaMeshi.form.ReviewForm;
import com.NagoyaMeshi.repository.ReviewRepository;

@Controller
@RequestMapping("/user/review")
public class UserReviewController {

private final ReviewRepository reviewRepository;
	
	public UserReviewController(ReviewRepository reviewRepository) {
	    this.reviewRepository = reviewRepository; 
	    
	    }	
	
	@GetMapping
	public String index(Model model, Pageable pageable) {	
		 Page<Review> reviewPage = reviewRepository.findAll(pageable);
  	 
      model.addAttribute("reviewPage", reviewPage); 
      
      
      return "/user/review/review";
  }  
	
	@PostMapping("/user/review/{shopId}/create")
	  public String createReview(@PathVariable int shopId, @ModelAttribute ReviewForm reviewForm) {
	    reviewService.saveReview(shopId, reviewForm);
	    return "redirect:/user/shop/" + shopId; // レビュー投稿後に店舗詳細ページに戻る
	  }
	
}
