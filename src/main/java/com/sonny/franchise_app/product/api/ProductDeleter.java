package com.sonny.franchise_app.product.api;

import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import com.sonny.franchise_app.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class ProductDeleter {

    private final ProductRepository productRepository;

    public Mono<Void> deleteProductById(Long id) {
        return productRepository.existsById(id)
                .flatMap(existsProduct -> {
                   if (!existsProduct)
                       return Mono.error(new ProductNotFoundException(id));
                   return productRepository.deleteById(id);
                })
                .doOnSuccess(f -> log.info("El producto se eliminó correctamente de la base de datos."))
                .doOnError(e -> log.error("Error al eliminar el producto", e));
    }
}
