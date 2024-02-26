package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.Favorite;
import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.entity.User;
import com.NagoyaMeshi.repository.FavoriteRepository;
import com.NagoyaMeshi.repository.ShopRepository;
import com.NagoyaMeshi.repository.UserRepository;
import com.NagoyaMeshi.security.UserDetailsImpl;
import com.NagoyaMeshi.service.FavoriteService;

@Controller
@RequestMapping("/user/favorite")
public class UserFavoriteController {

	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;  
	private final ShopRepository shopRepository;  
	private final FavoriteService favoriteService;
	
	public UserFavoriteController(FavoriteRepository favoriteRepository,
			UserRepository userRepository, 
			FavoriteService favoriteService,
			ShopRepository shopRepository) {
		
	    this.favoriteRepository = favoriteRepository; 
	    this.userRepository = userRepository; 
	    this.favoriteService = favoriteService;
	    this.shopRepository = shopRepository;
	    }	
	
	@GetMapping
	public String index(Model model, Pageable pageable,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){		
	Page<Favorite> favoritePage = favoriteRepository.findAll(pageable);
	
		model.addAttribute("favoritePage", favoritePage); 
       
     	return "/user/favorite/favorite";
   }
	
	@PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
		favoriteRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "お気に入り店舗を削除しました。");
        
        return "redirect:/user/favorite/favorite";
    } 
	

	
	 
	 @PostMapping("/user/shop/{shopId}/create")
		public String create(@PathVariable(name = "id") Integer id, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes, Model model) {
			Shop shop = shopRepository.getReferenceById(id);
			User user = userDetailsImpl.getUser();
			favoriteService.create(shop, user);
			redirectAttributes.addFlashAttribute("successMessage", "お気に入りに登録しました。");
			
			return "redirect:/user/favorite/favorite";
		}
	 
}



//@GetMapping
//public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
//		 						  @RequestParam(name = "keyword", required = false) String keyword,
//		 						 @RequestParam(name = "categoryId", required = false) String categoryId,
//		 						@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
//	 
//	User user = userDetailsImpl.getUser();