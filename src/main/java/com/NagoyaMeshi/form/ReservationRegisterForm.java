package com.NagoyaMeshi.form;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationRegisterForm {

	@NotNull(message = "時間を入力してください。")
	private LocalTime reservationTime;
	
	@NotNull(message = "日付を入力してください。")
	private LocalDate reservationDate;  
	
	@NotNull(message = "人数を入力してください。")
    @Min(value = 1, message = "人数は1人以上に設定してください。")
    private Integer numberOfPeople; 
}
