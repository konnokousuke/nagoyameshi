package com.example.nagoyameshi.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.form.MemberEditForm;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.repository.RoleRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final Map<String, Boolean> paidRegistrationFlags = new ConcurrentHashMap<>(); // 一時的に登録完了フラグを保持する

    public MemberService(MemberRepository memberRepository, RoleRepository roleRepository, 
                         PasswordEncoder passwordEncoder, VerificationTokenService verificationTokenService) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
    }

    // 無料会員の作成
    @Transactional
    public Member create(SignupForm signupForm) {
        Member member = new Member();
        Role role = roleRepository.findByName("ROLE_FREE");

        member.setName(signupForm.getName());
        member.setFurigana(signupForm.getFurigana());
        member.setPostalCode(signupForm.getPostalCode());
        member.setAddress(signupForm.getAddress());
        member.setPhoneNumber(signupForm.getPhoneNumber());
        member.setEmail(signupForm.getEmail());
        member.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        member.setRole(role);
        member.setEnabled(false);
        member.setStatus(Member.Status.FREE);  // 無料会員ステータス設定

        return memberRepository.save(member);
    }
/*
    // 有料会員の作成
    @Transactional
    public Member create(PaidSignupForm paidSignupForm) {
        Member member = new Member();
        Role role = roleRepository.findByName("ROLE_PAID");

        member.setName(paidSignupForm.getName());
        member.setFurigana(paidSignupForm.getFurigana());
        member.setPostalCode(paidSignupForm.getPostalCode());
        member.setAddress(paidSignupForm.getAddress());
        member.setPhoneNumber(paidSignupForm.getPhoneNumber());
        member.setEmail(paidSignupForm.getEmail());
        member.setPassword(passwordEncoder.encode(paidSignupForm.getPassword()));
        member.setRole(role);
        member.setEnabled(true);
        member.setStatus(Member.Status.PAID);  // 有料会員ステータス設定

        return memberRepository.save(member);
    }
*/
    @Transactional
    public void update(MemberEditForm memberEditForm) {
        Member member = memberRepository.getReferenceById(memberEditForm.getId());

        member.setName(memberEditForm.getName());
        member.setFurigana(memberEditForm.getFurigana());
        member.setPostalCode(memberEditForm.getPostalCode());
        member.setAddress(memberEditForm.getAddress());
        member.setPhoneNumber(memberEditForm.getPhoneNumber());
        member.setEmail(memberEditForm.getEmail());

        memberRepository.save(member);
    }

    // メールアドレスの登録済みチェック
    public boolean isEmailRegistered(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null;
    }

    // パスワード確認
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }

    // 会員の有効化
    @Transactional
    public void enableMember(Member member) {
        member.setEnabled(true);
        memberRepository.save(member);
    }

    // トークンの検証と会員の有効化
    @Transactional
    public Member verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null) {
            throw new IllegalArgumentException("無効なトークンです。");
        }

        Member member = verificationToken.getMember();
        enableMember(member);

        return member;
    }

    // メールアドレスが変更されたかのチェック
    public boolean isEmailChanged(MemberEditForm memberEditForm) {
        Member currentMember = memberRepository.getReferenceById(memberEditForm.getId());
        return !memberEditForm.getEmail().equals(currentMember.getEmail());
    }

    // メールアドレスでの会員検索
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // addressがnullまたは空の場合にデフォルト値を設定して保存するメソッド
    public Member saveMember(Member member) {
        if (member.getAddress() == null || member.getAddress().isEmpty()) {
            member.setAddress("");  // デフォルトで空文字列を設定
        }
        return memberRepository.save(member);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public void saveAndFlush(Member member) {
        memberRepository.saveAndFlush(member);
    }
    
    // customerIdで会員情報を取得
    public Member findByCustomerId(String customerId) {
        return memberRepository.findByCustomerId(customerId);
    }
    
 // 有料会員登録完了フラグを設定
    public void setPaidRegistrationFlag(String email, boolean flag) {
        paidRegistrationFlags.put(email, flag);
    }

    // 有料会員登録完了フラグが設定されているか確認
    public boolean isPaidRegistrationComplete(String email) {
        return paidRegistrationFlags.getOrDefault(email, false);
    }

    // 有料会員登録完了フラグをクリア
    public void clearPaidRegistrationFlag(String email) {
        paidRegistrationFlags.remove(email);
    }
    
    public void updateRoleToPaid(Member member) {
        Role paidRole = roleRepository.findByName("ROLE_PAID"); // 役割「ROLE_PAID」を取得
        if (paidRole != null) {
            member.setRole(paidRole); // 役割を有料会員に設定
            memberRepository.save(member); // データベースに保存して反映
        }
    }
}