package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortingType {
    //TODO divide for sorting directions and sort-by

    PRICE_ASCENDING("price_ascending"),
    PRICE_DESCENDING("price_descending"),
    RATE_ASCENDING("rate_ascending"),
    RATE_DESCENDING("rate_descending");

    @Getter
    private final String sortingType;
}
