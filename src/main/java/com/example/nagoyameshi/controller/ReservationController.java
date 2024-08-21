package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.security.MemberDetailsImpl;

@Controller
public class ReservationController {
	
	private final ReservationRepository reservationRepository;
	
	public ReservationController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
	
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "reservationId", direction = Direction.ASC) Pageable pageable, Model model) {
		Member member = memberDetailsImpl.getMember();
		Page<Reservation> reservationPage =reservationRepository.findByMemberOrderByReservationIdDesc(member, pageable);
		
		model.addAttribute("reservationPage", reservationPage);
		
		return "reservations/index";
	}
}
