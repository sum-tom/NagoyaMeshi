package com.NagoyaMeshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.Review;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.ReviewForm;
import com.NagoyaMeshi.repository.ReviewRepository;
@Service	
public class ReviewService {

	
private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	 @Transactional
	public void create(Shop shop, User user, ReviewForm reviewForm) {
		
		Review review = new Review();
	    review.setShop(shop); 
	    review.setUser(user); 
	    review.setReviewRating(reviewForm.getReviewRating());
	    review.setReviewComment(reviewForm.getReviewComment());
	    reviewRepository.save(review);
	  }

}
