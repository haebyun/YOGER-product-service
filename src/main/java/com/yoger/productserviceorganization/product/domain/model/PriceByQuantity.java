package com.yoger.productserviceorganization.product.domain.model;

import com.yoger.productserviceorganization.product.domain.exception.InvalidPriceException;
import com.yoger.productserviceorganization.product.domain.exception.InvalidQuantityException;

public record PriceByQuantity(
        int quantity,
        int price
) {
    public static PriceByQuantity of(Integer quantity, Integer price) {
        validate(quantity, price);
        return new PriceByQuantity(quantity, price);
    }

    private static void validate(Integer quantity, Integer price) {
        if (quantity == null || quantity < 0) {
            throw new InvalidQuantityException("수량은 0 이상의 정수를 입력해야 합니다.");
        }
        if (price == null || price < 0) {
            throw new InvalidPriceException("가격은 0 이상의 정수를 입력해야 합니다.");
        }
    }

    public Boolean isLargerThenSoldAmount(Integer soldAmount) {
        return soldAmount <= this.quantity;
    }
}
