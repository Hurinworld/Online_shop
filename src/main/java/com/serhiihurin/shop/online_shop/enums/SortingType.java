package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortingType {
    ASCENDING("ascending"),
    DESCENDING("descending");

    @Getter
    private final String sortingType;
}
