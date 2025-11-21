package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.franchise.dto.MaxStockProductDto;
import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.exception.FranchiseWithZeroBranchesException;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@AllArgsConstructor
@Service
public class MaxStockProductsProvider {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Flux<MaxStockProductDto> get(Long franchiseId) {

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise ->
                        branchRepository.findByFranchiseId(franchiseId)
                                .switchIfEmpty(Flux.error(
                                        new FranchiseWithZeroBranchesException(franchiseId)
                                ))
                                .flatMap(branch ->
                                        getMaxStockProductByBranch(branch)
                                                .subscribeOn(Schedulers.parallel())
                                )
                );
    }

    private Mono<MaxStockProductDto> getMaxStockProductByBranch(Branch branch) {
        return productRepository.findByBranchId(branch.getId())
                // Encontrar el producto con máximo stock
                .reduce((p1, p2) -> p1.getStock() > p2.getStock() ? p1 : p2)
                // Mapear al DTO
                .map(p -> MaxStockProductDto
                                .builder()
                                .productId(p.getId())
                                .productName(p.getName())
                                .productStock(p.getStock())
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .build()
                )
                .switchIfEmpty(Mono.empty()); // Si no hay productos, retornar vacío
    }
}
