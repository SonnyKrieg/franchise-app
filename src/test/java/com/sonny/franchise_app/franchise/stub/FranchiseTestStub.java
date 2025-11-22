package com.sonny.franchise_app.franchise.stub;


import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.entity.Franchise;
import com.sonny.franchise_app.franchise.request.CreateFranchiseRequest;

import java.util.HashSet;

public class FranchiseTestStub {

    public static Franchise getFranchiseWithNameAndId(Long id, String name) {
        return Franchise.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Franchise of(Long id, String name) {
        return Franchise.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Franchise getDefaultFranchise() {

        Long franchiseId = 1L;

        return Franchise.builder().id(franchiseId)
                .name("ABC1").build();
    }

    public static Franchise getFranchiseWithId(Long franchiseId) {

        return Franchise.builder().id(franchiseId)
                .name("ABC1").build();
    }

    public static FranchiseDto getFranchiseDto() {
        Long franchiseId = 1L;
        return new FranchiseDto(franchiseId, "ABC1", new HashSet<>());

    }

    public static CreateFranchiseRequest getCreateFranchiseRequest() {
        return new CreateFranchiseRequest("ABC1");
    }

}

