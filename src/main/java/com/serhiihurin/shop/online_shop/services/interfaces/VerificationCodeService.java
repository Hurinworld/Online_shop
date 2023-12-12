package com.serhiihurin.shop.online_shop.services.interfaces;

import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.entity.VerificationCode;

public interface VerificationCodeService {
    VerificationCode createVerificationCode(User linkedUserAccount);
}
