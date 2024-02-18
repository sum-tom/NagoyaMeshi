package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.Favorite;
import com.NagoyaMeshi.repository.FavoriteRepository;

@Controller
@RequestMapping("/user")
public class UserFavoriteController {

	private final FavoriteRepository favoriteRepository;
	
	public UserFavoriteController(FavoriteRepository favoriteRepository) {
	    this.favoriteRepository = favoriteRepository; 
	    
	    }	
	
	@GetMapping
	public String index(Model model, Pageable pageable) {		
	Page<Favorite> favoritePage = favoriteRepository.findAll(pageable);
		 	 
		model.addAttribute("favoritePage", favoritePage); 
       
     	return "/user/favorite";
   }
	
	@PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
		favoriteRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/user";
    } 
}
