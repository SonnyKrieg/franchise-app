package com.sonny.franchise_app.branch.exception;

import lombok.Getter;

@Getter
public class BranchDuplicatedNameException extends RuntimeException {

    private final String name;

    public BranchDuplicatedNameException(String name) {
        super(String.format("El nombre %s ya se encuentra registrado.", name));
        this.name = name;
    }
}

