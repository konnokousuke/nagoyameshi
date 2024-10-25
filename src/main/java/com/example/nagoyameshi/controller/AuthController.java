package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.service.MemberService;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final MemberService memberService;
    private final SignupEventPublisher signupEventPublisher;
    private final VerificationTokenService verificationTokenService;
    //private final StripeService stripeService;
    
    public AuthController(MemberService memberService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService, StripeService stripeService) {
        this.memberService = memberService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
        //this.stripeService = stripeService;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup"; // 無料会員用登録ページ
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (memberService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // パスワードとパスワード(確認用)の入力内容が一致していなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!memberService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        Member createdMember = memberService.create(signupForm);
        String requestUrl = httpServletRequest.getRequestURL().toString();
        signupEventPublisher.publishSignupEvent(createdMember, requestUrl);
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/";
    }

    @GetMapping("signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        String key = null;
        String message = null;

        if (verificationToken != null) {
            Member member = verificationToken.getMember();
            memberService.enableMember(member);
            key = "successMessage";
            message = "会員登録が完了しました。";
        } else {
            key = "errorMessage";
            message = "トークンが無効です。";
        }
        model.addAttribute(key, message);
        return "auth/verify";
    }
/*
    // 有料会員登録
    @GetMapping("/paid_signup")
    public String paidSignup(Model model) {
        model.addAttribute("paidSignupForm", new PaidSignupForm());
        return "auth/paid_signup"; // 有料会員用登録ページ
    }

    // データベースへの登録処理は行わず、確認画面への遷移のみを行う
    @PostMapping("/paid_signup/confirm")
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

    // StripeのCheckoutセッションを作成し、登録処理を行う
    @PostMapping("/paid_signup/session")
    public String createPaidSignupSession(@ModelAttribute("paidSignupForm") PaidSignupForm paidSignupForm, RedirectAttributes redirectAttributes) {
        try {
            // Memberの登録
            Member createdMember = memberService.create(paidSignupForm);

            // StripeのCheckoutセッションを作成
            String sessionUrl = stripeService.createCheckoutSession(createdMember, "price_1Pxgg1P8kPlKAx7ZxQpcEyq8").getUrl();

            // セッションURLにリダイレクト
            return "redirect:" + sessionUrl;
        } catch (StripeException e) {
            // エラーメッセージを追加
            redirectAttributes.addFlashAttribute("errorMessage", "支払い登録中にエラーが発生しました。");
            return "redirect:/auth/paid_signup";
        }
    }
    */
}
