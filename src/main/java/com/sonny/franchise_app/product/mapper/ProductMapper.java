package com.sonny.franchise_app.product.mapper;

import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.entity.Product;
import org.springframework.beans.BeanUtils;

public class ProductMapper {

    public static Product toEntity(ProductDto productDto) {

        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);

        return product;

    }

    public static ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);

        return productDto;
    }
}
