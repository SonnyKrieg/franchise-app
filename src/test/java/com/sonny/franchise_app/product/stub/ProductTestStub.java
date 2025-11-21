package com.sonny.franchise_app.product.stub;

import com.sonny.franchise_app.branch.request.AddProductRequest;
import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.entity.Product;

public class ProductTestStub {

    public static AddProductRequest getDefaultProductRequest() {

        Long productId = 1L;

        return AddProductRequest.builder()
                .name("product 1")
                .branchId(1L)
                .stock(11)
                .build();
    }

    public static Product getDefaultProduct() {

        Long productId = 1L;

        return Product.builder()
                .id(productId)
                .name("product 1")
                .branchId(1L)
                .stock(11)
                .build();
    }

    public static Product getProductWithId(Long productId) {

        return Product.builder()
                .id(productId)
                .name("product 1").
                branchId(1L)
                .stock(11)
                .build();
    }

    public static ProductDto getProductDto() {
        Long productId = 1L;
        return ProductDto.builder()
                .id(1L)
                .name("product 1")
                .branchId(1L)
                .stock(11)
                .build();
    }
}
