package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.form.MemberEditForm;
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
		
		return memberRepository.save(member);
	}
	
	@Transactional
	public void update(MemberEditForm memberEditForm) {
		// TODO Long.valueOf要らなくする
		Member member = memberRepository.getReferenceById(Long.valueOf(memberEditForm.getId()));
		
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
		// TODO Long.valueOf要らなくする
		Member currentMember = memberRepository.getReferenceById(Long.valueOf(memberEditForm.getId()));
		return !memberEditForm.getEmail().equals(currentMember.getEmail());
	}
}
