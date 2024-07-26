package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.repository.StoreRepository;

@Controller
@RequestMapping("/stores")
public class StoreController {
	private final StoreRepository storeRepository;
	
	public StoreController(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			            @RequestParam(name = "price", required = false) Integer price,
			            @RequestParam(name = "order", required = false) String order,
			            @PageableDefault(page =0, size = 10, sort = "storeId", direction = Direction.ASC) Pageable pageable,
			            Model model)
	{
		Page<Store> storePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				storePage = storeRepository.findByStoreNameLikeOrDescriptionLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
			} else {
				storePage = storeRepository.findByStoreNameLikeOrDescriptionLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			}
			
		} else if (price != null) {
			if (order != null && order.equals("priceAsc")) {
				storePage = storeRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				storePage = storeRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}
		} else {
			if (order != null && order.equals("priceAsc")) {
				storePage = storeRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				storePage = storeRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}
		
		model.addAttribute("storePage", storePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("price", price);
		model.addAttribute("order", order);
		
		return "stores/index";
	}
}
