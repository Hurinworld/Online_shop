package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortingParameter {

    PRICE("price"),
    RATE("rate");

    @Getter
    private final String sortingValue;
}
