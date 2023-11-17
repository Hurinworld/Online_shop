package com.serhiihurin.shop.online_shop.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SortingDirection {
    //TODO divide for sorting directions and sort-by //done

    ASCENDING("ascending"),
    DESCENDING("descending");

    @Getter
    private final String sortingDirection;
}
