package com.sonny.franchise_app.product.exception;

public class ProductNotFoundException extends RuntimeException {

    private final Long id;

    public ProductNotFoundException(Long id) {
        super(String.format("El producto con Id %s no se encontro.", id));
        this.id = id;
    }

}
