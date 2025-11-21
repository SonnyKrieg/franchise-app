package com.sonny.franchise_app.product.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateStockRequest {

    @Positive
    private int newStock;

}
