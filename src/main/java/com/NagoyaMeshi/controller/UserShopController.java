package com.NagoyaMeshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.repository.CategoryRepository;
import com.NagoyaMeshi.repository.ShopRepository;

@Controller
@RequestMapping("/user/shop")
public class UserShopController {

	private final ShopRepository shopRepository;
	private final CategoryRepository categoryRepository;
	
	public UserShopController(ShopRepository shopRepository,CategoryRepository categoryRepository) {
	    this.shopRepository = shopRepository; 
	    this.categoryRepository = categoryRepository; 
	    }	
	
	@GetMapping
	 public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
			 						  @RequestParam(name = "keyword", required = false) String keyword,
									  @RequestParam(name = "categoryId", required = false) Integer categoryId) {

		
		Page<Shop> shopPage;
       
		if (keyword != null && !keyword.isEmpty()) {
	        shopPage = shopRepository.findByNameLike("%" + keyword + "%", pageable);
	    } else if (categoryId != null) {
	        shopPage = shopRepository.findByCategoryId(categoryId, pageable);
	    } else {
	        shopPage = shopRepository.findAll(pageable);
	    }
		 
       model.addAttribute("shopPage", shopPage); 
       model.addAttribute("keyword", keyword);
       model.addAttribute("categories", categoryRepository.findAll());
       
       
       return "/user/shop/shop-list";
   }  
	
	@GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
		Shop shop = shopRepository.getReferenceById(id);
        
        model.addAttribute("shop", shop);         
        
        return "user/shop/shop-detail";
    }    
	
}
