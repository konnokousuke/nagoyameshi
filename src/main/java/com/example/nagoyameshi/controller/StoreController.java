package com.example.nagoyameshi.controller;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.StoreRepository;

import jakarta.validation.Valid;

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
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, Model model) {
        Store store = storeRepository.getReferenceById(id);
        
        model.addAttribute("store", store);
        model.addAttribute("reservationForm", new ReservationInputForm());
        
        return "stores/show";
    }
    
    @PostMapping("/{id}/reserve")
    public String confirmReservation(@Valid ReservationInputForm form, BindingResult bindingResult, Model model, @PathVariable("id") Long storeId) {
        if (bindingResult.hasErrors()) {
            return "stores/show";
        }

        Store store = storeRepository.getReferenceById(storeId);

        // 過去の日時を指定した場合のバリデーション
        if (form.getReservationDatetime().isBefore(LocalDateTime.now())) {
            model.addAttribute("errorMessage", "指定した日時は無効です。");
            return "stores/show";
        }

        // 営業終了一時間前以内を指定した場合のバリデーション
        LocalDateTime reservationLimit = form.getReservationDatetime().toLocalDate().atTime(store.getClosingTime().minusHours(1));
        if (form.getReservationDatetime().isAfter(reservationLimit)) {
            model.addAttribute("errorMessage", "営業終了一時間前以上を指定してください。");
            return "stores/show";
        }

        // 定休日を指定した場合のバリデーション
        DayOfWeek reservationDay = form.getReservationDatetime().getDayOfWeek();
        if (store.getDayOffs().contains(reservationDay)) {
            model.addAttribute("errorMessage", "定休日は指定できません。");
            return "stores/show";
        }

        // ユーザーが有料会員でない場合のバリデーション
        if (!isPaidMember()) {
            model.addAttribute("errorMessage", "予約するには有料会員への登録が必要です。");
            return "stores/show";
        }

        // 予約確認ページにデータを渡す
        model.addAttribute("reservationForm", form);
        return "stores/confirm";
    }

    private boolean isPaidMember() {
        // ログインユーザーが有料会員かどうかを確認するロジックを実装する
        return true; // 仮に有料会員とする
    }
}
