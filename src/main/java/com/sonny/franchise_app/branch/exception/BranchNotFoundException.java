package com.sonny.franchise_app.branch.exception;

import lombok.Getter;

@Getter
public class BranchNotFoundException extends RuntimeException {

    private final Long id;

    public BranchNotFoundException(Long id) {
        super(String.format("La sucursal con Id %s no se encontro.", id));
        this.id = id;
    }

}
