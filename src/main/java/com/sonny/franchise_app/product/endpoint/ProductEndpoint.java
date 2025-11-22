package com.sonny.franchise_app.product.endpoint;

import com.sonny.franchise_app.product.api.ProductDeleter;
import com.sonny.franchise_app.product.api.ProductUpdater;
import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.request.UpdateStockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = ProductEndpoint.PRODUCT_URL)
public class ProductEndpoint {

    public static final String PRODUCT_URL = "/api/v1/product";

    private final ProductDeleter productDeleter;
    private final ProductUpdater productUpdater;

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {

        log.info("Eliminando el producto con id {}", id);

        return productDeleter.deleteProductById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PatchMapping("/{id}/stock")
    public Mono<ResponseEntity<ProductDto>> updateStock(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStockRequest request) {

        log.info("Actualizando el stock del producto {} a {}", id, request.getNewStock());

        return productUpdater.updateStock(id, request)
                .map(ResponseEntity::ok);
    }
}