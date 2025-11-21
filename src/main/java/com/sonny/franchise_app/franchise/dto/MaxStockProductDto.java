package com.sonny.franchise_app.franchise.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MaxStockProductDto {

    private Long    productId;
    private String  productName;
    private Integer productStock;
    private Long    branchId;
    private String  branchName;

}
