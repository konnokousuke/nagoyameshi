package com.example.nagoyameshi.controller;
/*
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // 既存のバリデーションエラーの処理
        if (bindingResult.hasErrors()) {
            return "auth/paid_signup"; // エラーメッセージを表示して入力画面に戻る
        }

        // パスワードと確認用パスワードの一致チェック
        if (!paidSignupForm.getPassword().equals(paidSignupForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.paidSignupForm", "パスワードが一致しません。");
            return "auth/paid_signup"; // エラーを表示して入力画面に戻る
        }

        model.addAttribute("paidSignupForm", paidSignupForm);
        return "auth/paid_confirm"; // 確認画面に遷移
    }


    // 登録処理
    @PostMapping("/register")
    public String registerPaidSignup(@ModelAttribute("paidSignupForm") PaidSignupForm paidSignupForm,
                                     RedirectAttributes redirectAttributes) {
        Member createdMember = memberService.create(paidSignupForm);

        VerificationToken token = verificationTokenService.createVerificationToken(createdMember);
        signupEventPublisher.publishSignupEvent(createdMember, token.getToken());

        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

     // トップページにリダイレクトする
        return "redirect:/";
    }

    // StripeのCheckoutセッションを作成
    @PostMapping("/session")
    @ResponseBody
    public ResponseEntity<?> createPaidSignupSession(@RequestBody PaidSignupForm paidSignupForm) {
        try {
            Member member = new Member();
            member.setEmail(paidSignupForm.getEmail());
            member.setName(paidSignupForm.getName());
            member.setPhoneNumber(paidSignupForm.getPhoneNumber());

            Session session = stripeService.createCheckoutSession(member, "price_1Q12fwP8kPlKAx7ZumFFP0mT");

            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "支払い登録中にエラーが発生しました。"));
        }
    }
    
 // 支払い成功後に呼び出されるエンドポイント
    @RequestMapping("/success")
    public String paidSignupSuccess(@RequestParam("session_id") String sessionId, RedirectAttributes redirectAttributes) {
        try {
            // セッションIDが正しく取得できているかログ出力
            System.out.println("Received session ID: " + sessionId);

            // セッションIDに基づいてStripeのセッション情報を取得
            Session session = stripeService.retrieveSession(sessionId);

            // セッション情報からメールアドレスなどを取得し、Memberを作成
            Member member = memberService.findByEmail(session.getCustomerDetails().getEmail());
            
            if (member != null) {
                // 登録処理と認証メール送信
                VerificationToken token = verificationTokenService.createVerificationToken(member);
                signupEventPublisher.publishSignupEvent(member, token.getToken());
            }

            // フラッシュメッセージを設定
            redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "支払い後の処理中にエラーが発生しました。");
            return "redirect:/auth/paid_signup";  // エラーページへリダイレクト
        }

        System.out.println("Redirecting to the homepage..."); // ログ出力でリダイレクト確認
        return "redirect:/";  // トップページにリダイレクト
    }

}
*/