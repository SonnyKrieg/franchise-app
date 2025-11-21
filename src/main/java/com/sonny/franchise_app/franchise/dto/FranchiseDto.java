package com.sonny.franchise_app.franchise.dto;


import com.sonny.franchise_app.branch.entity.Branch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FranchiseDto {

    private Long id;
    private String name;
    private Set<Branch> branches;

}
