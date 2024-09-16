package com.example.nagoyameshi.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    // トークンの生成と保存
    public VerificationToken createVerificationToken(Member member) {
        String token = UUID.randomUUID().toString();
        return create(member, token); // 修正部分
    }

    // トークンの取得
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
    
    // 新しく追加する create メソッド
    public VerificationToken create(Member member, String token) {
        VerificationToken verificationToken = new VerificationToken(token, member);
        return verificationTokenRepository.save(verificationToken);
    }
}
