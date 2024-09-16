package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.form.MemberEditForm;
import com.example.nagoyameshi.form.PaidSignupForm;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.repository.MemberRepository;
import com.example.nagoyameshi.repository.RoleRepository;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public MemberService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	// 無料会員用
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
		member.setStatus(Member.Status.FREE); // 無料会員ステータスを設定
		
		return memberRepository.save(member);
	}
	
	 @Transactional
	    public Member save(Member member) {
	        return memberRepository.save(member);
	    }
	 
	// 有料会員用
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
		member.setEnabled(false);
		member.setStatus(Member.Status.PAID); // 有料会員ステータスを設定
		
		return memberRepository.save(member);
	}
	
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
	
	// メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		Member member = memberRepository.findByEmail(email);
		return member != null;
	}
	
	// パスワードとパスワード(確認用)の入力値が一致しているか判定する
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
	
	// ユーザーを有効にする
	@Transactional
	public void enableMember(Member member) {
		member.setEnabled(true);
		memberRepository.save(member);
	}
	
	// メールアドレスが変更されたかどうかをチェックする
	public boolean isEmailChanged(MemberEditForm memberEditForm) {
		Member currentMember = memberRepository.getReferenceById(memberEditForm.getId());
		return !memberEditForm.getEmail().equals(currentMember.getEmail());
	}
}
