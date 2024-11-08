package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	public Page<Reservation> findByMemberOrderByReservationIdDesc(Member member, Pageable pageable);
	
	 Page<Reservation> findByStore(Store store, Pageable pageable);
}
