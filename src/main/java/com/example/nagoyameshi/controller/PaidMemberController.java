package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.service.StripeService;
import com.stripe.exception.StripeException;

@Controller
@RequestMapping("/paid")
public class PaidMemberController {
	
	private final StripeService stripeService;
	
	public PaidMemberController(StripeService stripeService) {
        this.stripeService = stripeService;
    }
	
	 @GetMapping
	    public String getPaidContent(Model model, Member member) {
	        try {
	            stripeService.createSubscription(member, "price_1Pxgg1P8kPlKAx7ZxQpcEyq8"); // price_xxxxはStripeの価格ID
	            model.addAttribute("message", "支払い情報を更新しました。");
	        } catch (StripeException e) {
	            model.addAttribute("message", "支払い情報を更新中にエラーが発生しました。");
	        }
	        return "paid/paidContent";
	    }
}
