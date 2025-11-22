package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.mapper.FranchiseMapper;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.UpdateNameRequest;
import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import com.sonny.franchise_app.product.mapper.ProductMapper;
import com.sonny.franchise_app.product.request.UpdateStockRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class FranchiseUpdater {

    private final FranchiseRepository franchiseRepository;

    public Mono<FranchiseDto> updateName(Long id, UpdateNameRequest request) {

        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setName(request.getName());
                    return franchiseRepository.save(product);
                })
                .map(FranchiseMapper::toDto)
                .doOnSuccess(p -> log.info("Nomnbre de la franquicia {} actualizado a {}", id, request.getName()))
                .doOnError(e -> log.error("Error actualizando el nombre de la franquicia {}", id, e));
    }
}
