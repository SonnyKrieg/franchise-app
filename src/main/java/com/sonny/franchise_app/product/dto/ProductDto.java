package com.sonny.franchise_app.product.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private int  stock;
    private Long branchId;

}
