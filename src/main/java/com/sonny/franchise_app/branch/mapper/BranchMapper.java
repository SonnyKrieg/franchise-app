package com.sonny.franchise_app.branch.mapper;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.entity.Branch;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
public class BranchMapper {

    public static Branch toEntity(BranchDto branchDto) {

        Branch branch = new Branch();
        BeanUtils.copyProperties(branchDto, branch);

        return branch;

    }

    public static BranchDto toDto(Branch branch) {
        BranchDto branchDto = new BranchDto();
        BeanUtils.copyProperties(branch, branchDto);

        return branchDto;
    }
}
