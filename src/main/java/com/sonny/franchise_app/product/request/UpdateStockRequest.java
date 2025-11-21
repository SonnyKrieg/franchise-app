package com.sonny.franchise_app.product.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class UpdateStockRequest {

    @Positive
    private int newStock;

}
