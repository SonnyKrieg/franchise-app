package com.sonny.franchise_app.franchise.exception;

public class FranchiseNotFoundException extends RuntimeException {

    private final Long id;

    public FranchiseNotFoundException(Long id) {
        super(String.format("La franquicia con Id %s no se encontro.", id));
        this.id = id;
    }
}

