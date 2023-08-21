package com.serhiihurin.shop.online_shop.dao;

import com.serhiihurin.shop.online_shop.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    VerificationCode findByUserId(Long id);

    void deleteVerificationCodeByUserId (Long id);
}
