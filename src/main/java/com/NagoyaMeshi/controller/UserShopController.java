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
import com.NagoyaMeshi.form.ReservationRegisterForm;
import com.NagoyaMeshi.form.ReviewForm;
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
			 						 @RequestParam(name = "order", required = false) String order,
									  @RequestParam(name = "categoryId", required = false) Integer categoryId) {

		Page<Shop> shopPage;
		
		if (keyword != null && !keyword.isEmpty()) {	//keywordがnullでないかつ空でない場合
			
				if (order != null && order.equals("priceAsc")) {	//orderがnullでないかつorderがpriceAscと等しい場合
	            	shopPage = shopRepository.findByNameLikeOrderByPriceLowerLimitAsc("%" + keyword + "%",  pageable);	//名前が指定した文字列を含み、価格の下限が昇順でを検索する
	            } else {
					shopPage = shopRepository.findByNameLikeOrderByCreatedAtDesc("%" + keyword + "%",  pageable);	//名前が指定した文字列を含み、作成日時が新しい順に降順で検索する
	            }
			
			} else if (categoryId != null) {	//categoryIdがnullでないこと
			
				if (order != null && order.equals("priceAsc")) {
					shopPage = shopRepository.findByCategoryIdOrderByPriceLowerLimitAsc(categoryId, pageable);	//指定したカテゴリIDを検索、価格の下限が昇順でを検索する
		        } else {
		        	shopPage = shopRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);	//指定したカテゴリIDを検索、作成日時が新しい順に降順で検索する
		        }  
		
			  } else {
				  
				  if (order != null && order.equals("priceAsc")) {
						shopPage = shopRepository.findAllByOrderByPriceLowerLimitAsc( pageable);	//価格の下限が昇順でを検索する
			        } else {
			        	shopPage = shopRepository.findAllByOrderByCreatedAtDesc( pageable);	//作成日時が新しい順に降順で検索する
			        }  
			  }
//	        shopPage = shopRepository.findByNameLike("%" + keyword + "%", pageable);
//	    } else if (categoryId != null) {
//	        shopPage = shopRepository.findByCategoryId(categoryId, pageable);
//	    } else {
//	        shopPage = shopRepository.findAll(pageable);
//	    }
		 
       model.addAttribute("shopPage", shopPage); 
       model.addAttribute("keyword", keyword);
       model.addAttribute("categories", categoryRepository.findAll());
       model.addAttribute("order", order);
       
       return "/user/shop/shop-list";
   }  
	
	@GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
		Shop shop = shopRepository.getReferenceById(id);
        
        model.addAttribute("shop", shop);         
        model.addAttribute("reservationInputForm", new ReservationRegisterForm());
        model.addAttribute("reviewForm", new ReviewForm());
        
        
        return "user/shop/shop-detail";
    }    
	
}
