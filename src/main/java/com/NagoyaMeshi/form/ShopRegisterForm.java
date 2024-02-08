package com.NagoyaMeshi.form;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopRegisterForm {
	
	 private MultipartFile imageFile;
	
	@NotBlank(message = "店舗名を入力してください。")
    private String name;
    
    @NotBlank(message = "説明を入力してください。")
    private String description;   
    
    @NotNull(message = "上限価格帯を入力してください。")
    @Min(value = 1, message = "価格帯は1円以上に設定してください。")
    private Integer priceUpperLimit;  
    
    @NotNull(message = "下限価格帯を入力してください。")
    @Min(value = 1, message = "価格帯は1円以上に設定してください。")
    private Integer priceLowerLimit;  
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotNull(message = "開店時間を入力してください。")
    private LocalTime openingHours;
    
    @NotNull(message = "閉店時間を入力してください。")
    private LocalTime closingHours;
    
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
	
    @NotBlank(message = "定休日を入力してください。")
    private String regularHoliday;
    
    @NotNull(message = "カテゴリーを入力してください。")
    private Integer categoryId;
    
    
}
