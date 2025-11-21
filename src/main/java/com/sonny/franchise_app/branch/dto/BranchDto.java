package com.sonny.franchise_app.branch.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDto {

    private Long id;
    private String name;
    private Long franchiseId;

}
