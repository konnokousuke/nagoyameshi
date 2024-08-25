package com.example.nagoyameshi.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationInputForm {
	@NotNull(message = "予約日時を入力してください")
    private LocalDateTime reservationDatetime;

    @NotNull(message = "予約人数を入力してください")
    @Min(value = 1, message = "予約人数は1人以上である必要があります")
    private Integer numberOfPeople;

    // Getters and Setters
    public LocalDateTime getReservationDatetime() {
        return reservationDatetime;
    }

    public void setReservationDatetime(LocalDateTime reservationDatetime) {
        this.reservationDatetime = reservationDatetime;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
