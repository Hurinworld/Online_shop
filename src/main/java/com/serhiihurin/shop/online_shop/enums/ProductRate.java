package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductRate {
    VERY_BAD(1),
    BAD(2),
    AVERAGE(3),
    GOOD(4),
    AWESOME(5);

    @Getter
    private final int rateValue;
}
