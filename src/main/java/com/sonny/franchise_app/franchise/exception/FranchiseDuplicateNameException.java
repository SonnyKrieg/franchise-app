package com.sonny.franchise_app.franchise.exception;

import lombok.Getter;

@Getter
public class FranchiseDuplicateNameException extends RuntimeException {

    private final String name;

    public FranchiseDuplicateNameException(String name) {
        super(String.format("El nombre %s ya se encuentra registrado.", name));
        this.name = name;
    }
}