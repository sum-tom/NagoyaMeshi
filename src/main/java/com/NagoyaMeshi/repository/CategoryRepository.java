package com.NagoyaMeshi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.NagoyaMeshi.entity.Category;
public interface CategoryRepository extends JpaRepository<Category,Integer>{

}
