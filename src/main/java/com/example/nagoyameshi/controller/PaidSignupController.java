package com.example.nagoyameshi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.form.PaidSignupForm;
import com.example.nagoyameshi.service.MemberService;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.VerificationTokenService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth/paid_signup")
public class PaidSignupController {

    private final StripeService stripeService;
    private final MemberService memberService;
    private final VerificationTokenService verificationTokenService;
    private final SignupEventPublisher signupEventPublisher;

    public PaidSignupController(StripeService stripeService, MemberService memberService,
                                VerificationTokenService verificationTokenService, SignupEventPublisher signupEventPublisher) {
        this.stripeService = stripeService;
        this.memberService = memberService;
        this.verificationTokenService = verificationTokenService;
        this.signupEventPublisher = signupEventPublisher;
    }

    // 確認画面への遷移
    @PostMapping("/confirm")
    public String confirmPaidSignup(@Valid @ModelAttribute("paidSignupForm") PaidSignupForm paidSignupForm,
                                    BindingResult bindingResult, Model model) {
        // エラーチェック
        if (bindingResult.hasErrors()) {
            return "auth/paid_signup"; // エラーメッセージはビュー側で表示
        }

        // 確認画面にフォーム内容を渡す
        model.addAttribute("paidSignupForm", paidSignupForm);
        return "auth/paid_confirm";
    }

    // 登録処理
    @PostMapping("/register")
    public String registerPaidSignup(@ModelAttribute("paidSignupForm") PaidSignupForm paidSignupForm,
                                     RedirectAttributes redirectAttributes) {
        // 会員の作成
        Member createdMember = memberService.create(paidSignupForm);
        
        // 認証トークンを生成し、メールを送信
        VerificationToken token = verificationTokenService.createVerificationToken(createdMember);
        signupEventPublisher.publishSignupEvent(createdMember, token.getToken());

        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/";
    }

    // StripeのCheckoutセッションを作成
    @PostMapping("/session")
    @ResponseBody
    public ResponseEntity<?> createPaidSignupSession(@ModelAttribute("paidSignupForm") PaidSignupForm paidSignupForm) {
        try {
            // 一時的なMemberオブジェクトを作成
            Member member = new Member();
            member.setEmail(paidSignupForm.getEmail());
            member.setName(paidSignupForm.getName());
            member.setPhoneNumber(paidSignupForm.getPhoneNumber());

            // StripeのCheckoutセッションを作成
            Session session = stripeService.createCheckoutSession(member, "price_1Pxgg1P8kPlKAx7ZxQpcEyq8");

            // セッションURLをJSON形式で返却
            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (StripeException e) {
            // エラーが発生した場合、エラーメッセージを返す
            return ResponseEntity.badRequest().body("支払い登録中にエラーが発生しました。");
        }
    }
}
