package com.sonny.franchise_app.branch.api;

import com.sonny.franchise_app.branch.exception.BranchNotFoundException;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.request.AddProductRequest;
import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.entity.Product;
import com.sonny.franchise_app.product.exception.ProductDuplicateNameException;
import com.sonny.franchise_app.product.mapper.ProductMapper;
import com.sonny.franchise_app.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class AddProductToBranchHelper {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<ProductDto> addNewProductToBranch(Long branchId, AddProductRequest request) {

        return branchRepository.existsById(branchId)
                .flatMap(existsBranch -> {
                    if (!existsBranch)
                        return Mono.error(new BranchNotFoundException(branchId));
                    return productRepository.existsByNameAndBranchId(request.getName(), branchId);
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(
                                new ProductDuplicateNameException(branchId, request.getName()));
                    }

                    Product product = Product.builder()
                            .name(request.getName())
                            .branchId(branchId)
                            .stock(request.getStock())
                            .build();

                    return productRepository.save(product);

                })
                .map(ProductMapper::toDto)
                .doOnSuccess(f -> log.info("La sucursal se guardó correctamente en la base de datos."))
                .doOnError(e -> log.error("Error al guardar la sucursal", e));
    }
}
