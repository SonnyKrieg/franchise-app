package com.sonny.franchise_app.franchise.entity;

import com.sonny.franchise_app.branch.entity.Branch;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "franchise")
public class Franchise {

    @Id
    private Long id;

    private String name;

    @Transient
    private Set<Branch> branches;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Version
    private int version;

}
