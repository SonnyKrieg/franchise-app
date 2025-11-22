package com.sonny.franchise_app.branch.stub;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.franchise.request.AddBranchRequest;

public class BranchTestStub {


    public static Branch getBranchWithNameAndId(String name, long id) {

        return Branch.builder()
                .id(id)
                .name(name)
                .franchiseId(1L)
                .build();
    }

    public static Branch of(Long id, String name,
                            Long franchiseId) {
        return Branch.builder()
                .id(id)
                .name(name)
                .franchiseId(franchiseId)
                .build();
    }

    public static AddBranchRequest getDefaultBranchRequest() {

        Long branchId = 1L;

        return AddBranchRequest.builder()
                .name("ABC1 -- street 1")
                .build();
    }

    public static Branch getDefaultBranch() {

        Long branchId = 1L;

        return Branch.builder()
                .id(branchId)
                .name("ABC1 -- street 1")
                .franchiseId(1L)
                .build();
    }

    public static Branch getBranchWithId(Long branchId) {

        return Branch.builder()
                .id(branchId)
                .name("ABC1 -- street 1").
                franchiseId(1L)
                .build();
    }

    public static BranchDto getBranchDto() {
        Long branchId = 1L;
        return BranchDto.builder()
                .id(1L)
                .name("ABC1 -- street 1")
                .franchiseId(1L)
                .build();
    }
}
