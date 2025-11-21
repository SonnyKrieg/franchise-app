package com.sonny.franchise_app.product.exception;

import lombok.Getter;

@Getter
public class ProductDuplicateNameException extends RuntimeException {

    private final String name;

    public ProductDuplicateNameException(String name) {
        super(String.format("El nombre %s ya se encuentra registrado.", name));
        this.name = name;
    }

}