package com.NagoyaMeshi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NagoyaMeshi.entity.Category;
import com.NagoyaMeshi.form.CategoryEditForm;
import com.NagoyaMeshi.form.CategoryRegisterForm;
import com.NagoyaMeshi.repository.CategoryRepository;

@Service
public class CategoryService {
private final CategoryRepository categoryRepository;    
    
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;      

    }
    
    @Transactional
	public void create(CategoryRegisterForm categoryRegisterForm) {
    	Category category = new Category(); 
		
    
    	category.setName(categoryRegisterForm.getName());                
	    
	    categoryRepository.save(category);
    
	} 
    
    @Transactional
	public void update(CategoryEditForm categoryEditForm) {
    	Category category = new Category(); 
		
    
    	category.setName(categoryEditForm.getName());                
	    
	    categoryRepository.save(category);
    
	}

	
}
