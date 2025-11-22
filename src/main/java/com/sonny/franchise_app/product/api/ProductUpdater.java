package com.sonny.franchise_app.product.api;

import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import com.sonny.franchise_app.product.mapper.ProductMapper;
import com.sonny.franchise_app.product.repository.ProductRepository;
import com.sonny.franchise_app.product.request.UpdateProductNameRequest;
import com.sonny.franchise_app.product.request.UpdateStockRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class ProductUpdater {

    private final ProductRepository productRepository;

    public Mono<ProductDto> updateStock(Long id, UpdateStockRequest request) {

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setStock(request.getNewStock());
                    return productRepository.save(product);
                })
                .map(ProductMapper::toDto)
                .doOnSuccess(p -> log.info("Stock del producto {} actualizado a {}", id, request.getNewStock()))
                .doOnError(e -> log.error("Error actualizando el stock del producto {}", id, e));
    }

    public Mono<ProductDto> updateName(Long id, UpdateProductNameRequest request) {

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    product.setName(request.getName());
                    return productRepository.save(product);
                })
                .map(ProductMapper::toDto)
                .doOnSuccess(p -> log.info("Nomnbre del producto {} actualizado a {}", id, request.getName()))
                .doOnError(e -> log.error("Error actualizando el nombre del producto {}", id, e));
    }
}
