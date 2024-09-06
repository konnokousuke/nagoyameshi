package com.example.nagoyameshi.controller;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Store;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.StoreRepository;
import com.example.nagoyameshi.security.MemberDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/stores")
public class StoreController {
    private final StoreRepository storeRepository;
    private final ReservationService reservationService;

    public StoreController(StoreRepository storeRepository, ReservationService reservationService) {
        this.storeRepository = storeRepository;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "price", required = false) Integer price,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "storeId", direction = Direction.ASC) Pageable pageable,
                        Model model) {
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
        Optional<Store> storeOptional = storeRepository.findById(id);
        if (storeOptional.isPresent()) {
            Store store = storeOptional.get();
            model.addAttribute("store", store);
            model.addAttribute("reservationInputForm", new ReservationInputForm());

            // ログインユーザーが有料会員かどうかを判断
            model.addAttribute("isPaidMember", isPaidMember());
        } else {
            model.addAttribute("errorMessage", "指定された店舗が見つかりません。");
            return "stores/error";
        }

        return "stores/show";
    }

    @PostMapping("/{id}/reserve")
    public String confirmReservation(@Valid ReservationInputForm form, BindingResult bindingResult, Model model, @PathVariable("id") Long storeId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Store> storeOptional = storeRepository.findById(storeId);
            if (!storeOptional.isPresent()) {
                model.addAttribute("errorMessage", "指定された店舗が見つかりません。");
                return "stores/error";
            }

            Store store = storeOptional.get();
            model.addAttribute("store", store);
            model.addAttribute("isPaidMember", isPaidMember());
            
            if (bindingResult.hasErrors()) {
                model.addAttribute("errorMessage", "入力にエラーがあります。");
                model.addAttribute("validationErrors", bindingResult.getAllErrors());
                return "stores/show";
            }

            // 営業終了時間や定休日などのバリデーションロジックを追加
            String errorMessage = reservationService.isReservationDateTimeValid(store, form.getReservationDatetime());
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
                return "stores/show";
            }

            // 予約情報の作成と保存
            Reservation reservation = new Reservation();
            reservation.setReservationDatetime(Timestamp.valueOf(form.getReservationDatetime()));
            reservation.setNumberOfPeople(form.getNumberOfPeople());
            reservation.setStore(store);

            MemberDetailsImpl memberDetails = (MemberDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            reservationService.create(reservation, memberDetails.getMember().getId(), storeId);

            // 予約完了後にリダイレクトし、`reserved` パラメータをクエリに追加
            redirectAttributes.addAttribute("reserved", true);
            return "redirect:/reservations";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "予期しないエラーが発生しました。もう一度お試しください。");
            return "stores/show";
        }
    }

    
    @PostMapping("/reservation/confirm")
    public String confirmReservation(@ModelAttribute ReservationInputForm reservationInputForm, Model model) {
        // 予約確認のロジックを追加
        // 例: 必要なデータをモデルに追加
        model.addAttribute("reservationInputForm", reservationInputForm);
        return "reservation/confirm"; // 確認ページのテンプレート名を返す
    }

    private boolean isPaidMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof MemberDetailsImpl) {
            MemberDetailsImpl userDetails = (MemberDetailsImpl) principal;
            return userDetails.isPaidMember();
        }

        return false;
    }
}
