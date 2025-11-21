package com.sonny.franchise_app.branch.repository;

import com.sonny.franchise_app.branch.entity.Branch;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<Branch, Long> {

    public Mono<Boolean> existsByName(String name);
}
