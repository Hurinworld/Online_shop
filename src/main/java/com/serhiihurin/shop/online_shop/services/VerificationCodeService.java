package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;

public interface VerificationCodeService {
    VerificationCode createVerificationCode(User linkedUserAccount);
}
