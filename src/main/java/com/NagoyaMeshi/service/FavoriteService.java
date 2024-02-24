package com.NagoyaMeshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.Favorite;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.repository.FavoriteRepository;
@Service
public class FavoriteService {

private final FavoriteRepository favoriteRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	@Transactional
	public void create(Shop shop, User user) {
		Favorite favorite = new Favorite();
		
		favorite.setShop(shop);
		favorite.setUser(user);
		
		favoriteRepository.save(favorite);
	}
	
}
