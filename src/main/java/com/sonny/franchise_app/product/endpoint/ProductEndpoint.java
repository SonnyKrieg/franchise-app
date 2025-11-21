package com.sonny.franchise_app.product.endpoint;

import com.sonny.franchise_app.product.api.ProductDeleter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = ProductEndpoint.PRODUCT_URL)
public class ProductEndpoint {

    public static final String PRODUCT_URL = "/api/v1/product";

    private final ProductDeleter productDeleter;

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {

        log.info("Eliminando el producto con id {}", id);

        return productDeleter.deleteProductById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}