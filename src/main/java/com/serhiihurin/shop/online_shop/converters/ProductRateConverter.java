package com.serhiihurin.shop.online_shop.converters;
import com.serhiihurin.shop.online_shop.enums.ProductRate;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import jakarta.persistence.AttributeConverter;import jakarta.persistence.Converter;
@Converter
public class ProductRateConverter implements AttributeConverter<ProductRate, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductRate productRate) {
        return productRate != null ? productRate.getRateValue() : null;
    }

    @Override public ProductRate convertToEntityAttribute(Integer rate) {
        if (rate == null) {
            return null;
        }

        for (ProductRate productRate : ProductRate.values()) {
            if (productRate.getRateValue() == rate) {
                return productRate;
            }
        }

        throw new ApiRequestException("Unknown database value: " + rate);
    }
}
