package com.sonny.franchise_app.franchise.entity;

import org.springframework.data.annotation.*;

import java.time.Instant;
import java.util.Set;

public class Franchise {

    @Id
    private Long id;

    private String name;


    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Version
    private int version;

}
