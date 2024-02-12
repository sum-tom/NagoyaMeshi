package com.NagoyaMeshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Favorite;
public interface FavoriteRepository extends JpaRepository<Favorite,Integer>{

}
