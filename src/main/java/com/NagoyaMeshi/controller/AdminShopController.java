  package com.NagoyaMeshi.controller;
  
  import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NagoyaMeshi.entity.Shop;
import com.NagoyaMeshi.form.ShopEditForm;
import com.NagoyaMeshi.form.ShopRegisterForm;
import com.NagoyaMeshi.repository.CategoryRepository;
import com.NagoyaMeshi.repository.ShopRepository;
import com.NagoyaMeshi.service.ShopService;

@Controller
@RequestMapping("/admin/shop")
public class AdminShopController {
	private final ShopRepository shopRepository;
	private final ShopService shopService; 
	private final CategoryRepository categoryRepository;

	
	public AdminShopController(ShopRepository shopRepository, ShopService shopService,CategoryRepository categoryRepository) {
    this.shopRepository = shopRepository; 
    this.shopService = shopService;
    this.categoryRepository = categoryRepository; 
    }	
	
	@GetMapping
	 public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		 Page<Shop> shopPage;
        
		 if (keyword != null && !keyword.isEmpty()) {
			 shopPage = shopRepository.findByNameLike("%" + keyword + "%", pageable);                
         } else {
        	 shopPage = shopRepository.findAll(pageable);
         }  
		 
        model.addAttribute("shopPage", shopPage); 
        model.addAttribute("keyword", keyword);
        
        
        return "/admin/shop/admin-shop-list";
    }  
	
	@PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
        shopRepository.deleteById(id);
                
        redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
        
        return "redirect:/admin/shop";
    } 
	
	@GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("shopRegisterForm", new ShopRegisterForm());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/shop/register";
    } 
	
	 @PostMapping("/create")
	 public String create(@ModelAttribute @Validated ShopRegisterForm shopRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
	     if (bindingResult.hasErrors()) {
	         return "admin/shop/register";
	 }
	 
	 shopService.create(shopRegisterForm);
	 redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");    
	 
	 return "redirect:/admin/shop";
     }    
	 
	 @GetMapping("/{id}/edit")
     public String edit(@PathVariable(name = "id") Integer id, Model model) {
		 Shop shop = shopRepository.getReferenceById(id);
         String imageName = shop.getImageName();
         ShopEditForm shopEditForm = new ShopEditForm
        		 (
        	    	     shop.getId(),
        	    	     null,
        				 shop.getName(),
        				 shop.getDescription(),
        				 shop.getPriceUpperLimit(), 
        				 shop.getPriceLowerLimit(), 
        				 shop.getPostalCode(), 
        				 shop.getAddress(), 
        				 shop.getOpeningHours(), 
        				 shop.getClosingHours(), 
        				 shop.getPhoneNumber(), 
        				 shop.getRegularHoliday(), 
        				 shop.getCategoryId()
        				 );
         
         model.addAttribute("imageName", imageName);
         model.addAttribute("shopEditForm", shopEditForm);
         model.addAttribute("categories", categoryRepository.findAll());
         
         return "admin/shop/edit";
     }   
	 
	 @PostMapping("/{id}/update")
     public String update(@ModelAttribute @Validated ShopEditForm shopEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/shop/edit";
         }
         
         shopService.update(shopEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
         
         return "redirect:/admin/shop";
     }    
}
