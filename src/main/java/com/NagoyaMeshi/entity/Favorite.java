package com.NagoyaMeshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	 
	@ManyToOne
	@JoinColumn(name = "shop_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Shop shop; 
	    
	@Column(name = "shop_id")
    private Integer shopId;
	
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

}
