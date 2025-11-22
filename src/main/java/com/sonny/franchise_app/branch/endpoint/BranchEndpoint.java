package com.sonny.franchise_app.branch.endpoint;

import com.sonny.franchise_app.branch.api.AddProductToBranchHelper;
import com.sonny.franchise_app.branch.api.BranchUpdater;
import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.request.AddProductRequest;
import com.sonny.franchise_app.branch.request.UpdateBranchNameRequest;
import com.sonny.franchise_app.product.dto.ProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = BranchEndpoint.BRANCH_URL)
public class BranchEndpoint {

    public static final String BRANCH_URL = "/api/v1/branch";

    private final AddProductToBranchHelper helper;
    private final BranchUpdater branchUpdater;

    @PostMapping("/product/add")
    public Mono<ResponseEntity<ProductDto>> addProduct(@Valid @RequestBody Mono<AddProductRequest> requestMono) {
        return requestMono.flatMap(request -> {

            if (request.getName() == null) {
                log.warn("Solicitud de creación invalida: Producto sin nombre");
                return Mono.just(ResponseEntity.badRequest().<ProductDto>build());
            }

            log.info("Creando el producto con nombre {}", request.getName());

            return helper.addNewProductToBranch(request)
                    .doOnNext(productDto ->
                            log.info("El producto con nombre {} se creó correctamente", productDto.getName())
                    )
                    .map(productDto ->
                            ResponseEntity.status(HttpStatus.CREATED).body(productDto)
                    );
        });
    }

    @PatchMapping("/{id}/name")
    public Mono<ResponseEntity<BranchDto>> updateName(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBranchNameRequest request) {

        log.info("Actualizando el nombre de la franquicia {} a {}", id, request.getName());

        return branchUpdater.updateName(id, request)
                .map(ResponseEntity::ok);
    }

}