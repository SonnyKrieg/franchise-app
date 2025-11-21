package com.sonny.franchise_app.franchise.exception;

import lombok.Getter;

@Getter
public class FranchiseWithZeroBranchesException extends RuntimeException {

    private final Long id;

    public FranchiseWithZeroBranchesException(Long id) {
        super(String.format("La franquicia con Id %s no tiene sucursales.", id));
        this.id = id;
    }
}
