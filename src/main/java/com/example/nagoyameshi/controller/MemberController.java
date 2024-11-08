package com.example.nagoyameshi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.form.MemberEditForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.security.MemberDetailsImpl;
import com.example.nagoyameshi.service.MemberService;
import com.example.nagoyameshi.service.StripeService;
import com.stripe.exception.StripeException;

@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final StripeService stripeService;
    private final RoleRepository roleRepository;

    public MemberController(MemberRepository memberRepository, MemberService memberService, StripeService stripeService, RoleRepository roleRepository) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.stripeService = stripeService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String index(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl, Model model) {
        Member member = memberRepository.getReferenceById(memberDetailsImpl.getMember().getId());
        model.addAttribute("member", member);

        // 有料会員登録フラグを確認し、トーストメッセージを設定
        if (member.getStatus() == Member.Status.PAID && memberService.isPaidRegistrationComplete(member.getEmail())) {
            model.addAttribute("successMessage", "有料会員登録が完了しました。ログインし直してください。");
            // トーストメッセージを表示後、フラグをクリア
            memberService.clearPaidRegistrationFlag(member.getEmail());
        }

        return "member/index";
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl, Model model) {
        Member member = memberRepository.getReferenceById(memberDetailsImpl.getMember().getId());
        MemberEditForm memberEditForm = new MemberEditForm(member.getId(), member.getName(), member.getFurigana(),
                member.getPostalCode(), member.getAddress(), member.getPhoneNumber(), member.getEmail());

        model.addAttribute("memberEditForm", memberEditForm);

        return "member/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute @Validated MemberEditForm memberEditForm, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (memberService.isEmailChanged(memberEditForm) && memberService.isEmailRegistered(memberEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "member/edit";
        }

        memberService.update(memberEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

        return "redirect:/member";
    }

    @Transactional
    @PostMapping("/downgrade/confirm")
    public String downgradeConfirm(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl, RedirectAttributes redirectAttributes) {
        Member member = memberRepository.getReferenceById(memberDetailsImpl.getMember().getId());

        try {
            if (member.getCustomerId() != null) {
                // サブスクリプションのキャンセル
                stripeService.cancelSubscription(member.getCustomerId());

                // クレジットカード情報の削除
                stripeService.deleteCustomerCard(member.getCustomerId());

                // データベースの`customer_id`をnullに設定
                member.setCustomerId(null);
            }

            // 無料会員ステータスと役割に変更
            member.setStatus(Member.Status.FREE); // `status`を更新
            Role freeRole = roleRepository.findByName("ROLE_FREE");
            if (freeRole != null) {
                member.setRole(freeRole); // `role_id`を更新
            }

            // データベースへ保存
            memberRepository.saveAndFlush(member);
            
            // 成功メッセージを設定
            redirectAttributes.addFlashAttribute("successMessage", "有料会員から無料会員への手続きが完了しました。");
        } catch (StripeException e) {
            // Stripe関連エラーの処理
            redirectAttributes.addFlashAttribute("errorMessage", "Stripeとの通信に失敗しました。再度お試しください。");
            return "redirect:/member";
        } catch (Exception e) {
            // その他のエラー
            redirectAttributes.addFlashAttribute("errorMessage", "エラーが発生しました。もう一度お試しください。");
            return "redirect:/member";
        }

        return "redirect:/member";
    }
}
