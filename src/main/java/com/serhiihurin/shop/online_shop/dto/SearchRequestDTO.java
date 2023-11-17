package com.serhiihurin.shop.online_shop.dto;

import com.serhiihurin.shop.online_shop.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchRequestDTO {
    private User currentAuthenticatedUser;
    private String productName;
    private String sortingParameterValue;
    private String sortingDirection;
    private Double minimalPrice;
    private Double maximalPrice;
}
