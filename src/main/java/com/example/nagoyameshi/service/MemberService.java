package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.Role;
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
		member.setEnabled(true);
		
		return memberRepository.save(member);
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
}
