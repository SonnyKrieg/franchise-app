package com.sonny.franchise_app.product.exception;

import lombok.Getter;

@Getter
public class ProductDuplicateNameException extends RuntimeException {

    private final String name;
    private final Long branchId;

    public ProductDuplicateNameException(Long branchId, String name) {
        super(String.format("La sucursal %s ya cuenta con un producto llamado %s.", branchId, name));
        this.name = name;
        this.branchId = branchId;
    }

}