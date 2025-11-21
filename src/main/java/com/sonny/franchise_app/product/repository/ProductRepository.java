package com.sonny.franchise_app.product.repository;

import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.product.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    public Mono<Boolean> existsByName(String name);
    public Flux<Product> findByBranchId(Long id);

}
