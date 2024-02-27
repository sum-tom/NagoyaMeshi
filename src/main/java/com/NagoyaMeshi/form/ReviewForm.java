package com.NagoyaMeshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ReviewForm {


	@NotBlank(message = "レビュー評価してください。")
	private String reviewRating;
   
   @NotBlank(message = "テキストを入力してください。")
   private String reviewComment;  
   
   
}
