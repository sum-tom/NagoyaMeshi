package com.NagoyaMeshi.service;

import com.NagoyaMeshi.entity.Review;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.form.ReviewForm;
import com.NagoyaMeshi.repository.ReviewRepository;


public class ReviewService {

	
private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	
	public void saveReview(Shop shop, User user, ReviewForm reviewForm) {
	    Review review = new Review();
	    review.setShop(shop); // ショップIDを使ってShopオブジェクトを作成
	    review.setUser(user); // ログインしているユーザー情報をセット
	    review.setReviewRating(reviewForm.getReviewRating());
	    review.setReviewComment(reviewForm.getReviewComment());
	    reviewRepository.save(review);
	  }
	
}
