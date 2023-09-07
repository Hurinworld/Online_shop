package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.VerificationCodeRepository;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService{
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    @Transactional
    public VerificationCode createVerificationCode(User linkedUserAccount) {
        if (verificationCodeRepository.findByUserId(linkedUserAccount.getId()).isPresent()) {
            verificationCodeRepository.deleteVerificationCodeByUserId(linkedUserAccount.getId());
        }
        return verificationCodeRepository.save(
                VerificationCode.builder()
                .verificationCode(generateVerificationCode())
                .user(linkedUserAccount)
                .creationTime(LocalDateTime.now())
                .build()
        );
    }

    private String generateVerificationCode() {
        int min = 100000;
        int max = 999999;

        Random random = new Random();
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
