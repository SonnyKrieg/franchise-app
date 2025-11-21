package com.sonny.franchise_app.franchise.repository;

import com.sonny.franchise_app.franchise.entity.Franchise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends ReactiveCrudRepository<Franchise, Long> {

    public Mono<Boolean> existsByName(String name);


}
