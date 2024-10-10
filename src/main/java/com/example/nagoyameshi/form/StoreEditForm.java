package com.example.nagoyameshi.form;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreEditForm {
	@NotNull
	private Long storeId;
	
	@NotBlank(message = "店舗名を入力してください。")
	private String name;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "説明を入力してください。")
	private String description;
	
	@NotNull(message = "価格を入力してください。")
	@Min(value = 1, message = "価格を1円以上に設定してください。" )
	private Long price;
	
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;
	
	@NotBlank(message = "営業時間を入力してください。")
	private String openingHours;
	
	@NotBlank(message = "定休日を入力してください。")
	private String closedDays;
	
	private LocalTime closingTime; // 営業終了時間
    private Set<DayOfWeek> dayOffs; // 定休日

    // Getters and Setters
    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Set<DayOfWeek> getDayOffs() {
        return dayOffs;
    }

    public void setDayOffs(Set<DayOfWeek> dayOffs) {
        this.dayOffs = dayOffs;
    }

	public StoreEditForm(Long storeId2, String storeName, Object object, String description2, Long valueOf,
			String postalCode2, String address2, String phoneNumber2, String openingHours2, String closedDays2) {
		// TODO 自動生成されたコンストラクター・スタブ
	}
}
