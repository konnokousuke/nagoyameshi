package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.StoreEditForm;
import com.example.nagoyameshi.form.StoreRegisterForm;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.service.StoreService;

@Controller
@RequestMapping("/admin/stores")
public class AdminStoreController {
	private final StoreRepository storeRepository;
	private final StoreService storeService;
	
	public AdminStoreController(StoreRepository storeRepository, StoreService storeService) {
		this.storeRepository = storeRepository;
		this.storeService = storeService;
	}
	
	@GetMapping
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "storeId", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword, @ModelAttribute("successMessage") String successMessage) {
		Page<Store> storePage;
		
		if (keyword != null && !keyword.isEmpty()) {
			storePage = storeRepository.findByStoreNameLike("%" + keyword + "%", pageable);
		} else {
			storePage = storeRepository.findAll(pageable);
		}
		
		model.addAttribute("storePage", storePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("successMessage", successMessage); // Flashメッセージをモデルに追加
		
		return "admin/stores/index";
	}
	
	@GetMapping("{id}")
	public String show(@PathVariable(name = "id") Long id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		
		model.addAttribute("store", store);
		
		return "admin/stores/show";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("storeRegisterForm", new StoreRegisterForm());
		return "admin/stores/register";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated StoreRegisterForm storeRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/stores/register";
		}
		
		storeService.create(storeRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");
		
		return "redirect:/admin/stores";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Long id, Model model) {
		Store store = storeRepository.getReferenceById(id);
		String imageName = store.getImageFilename();
		StoreEditForm storeEditForm = new StoreEditForm(store.getStoreId(), store.getStoreName(), null, store.getDescription(), Long.valueOf(store.getPrice()), store.getPostalCode(), store.getAddress(), store.getPhoneNumber(), store.getOpeningHours(), store.getClosedDays());
		
		model.addAttribute("imageName", imageName);
		model.addAttribute("storeEditForm", storeEditForm);
		
		return "admin/stores/edit";
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated StoreEditForm storeEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/stores/edit";
		}
		
		storeService.update(storeEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を更新しました。");
		
		return "redirect:/admin/stores";
 	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
		storeRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
		
		return "redirect:/admin/stores";
	}
}