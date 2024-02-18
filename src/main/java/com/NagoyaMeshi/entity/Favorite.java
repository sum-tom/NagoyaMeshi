package com.NagoyaMeshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name =  "favorites")
@Data

public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	 
	@OneToOne
	@JoinColumn(name = "shop_id")
	private Shop shop; 
	    
//	@Column(name = "shop_id")
//    private Integer shopId;
	
	@OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
//    @Column(name = "user_id")
//    private Integer userId;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

}
