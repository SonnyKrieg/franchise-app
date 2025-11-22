package com.sonny.franchise_app.franchise.endpoint;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.franchise.api.AddBranchToFranchiseHelper;
import com.sonny.franchise_app.franchise.api.FranchiseCreator;
import com.sonny.franchise_app.franchise.api.FranchiseUpdater;
import com.sonny.franchise_app.franchise.api.MaxStockProductsProvider;
import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.dto.MaxStockProductDto;
import com.sonny.franchise_app.franchise.request.AddBranchRequest;
import com.sonny.franchise_app.franchise.request.CreateFranchiseRequest;
import com.sonny.franchise_app.franchise.request.UpdateFranchiseNameRequest;
import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.request.UpdateStockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = FranchiseEndpoint.FRANCHISE_URL)
public class FranchiseEndpoint {

    public static final String FRANCHISE_URL = "/api/v1/franchise";

    private final FranchiseCreator franchiseCreator;
    private final FranchiseUpdater franchiseUpdater;
    private final AddBranchToFranchiseHelper branchToFranchiseHelper;
    private final MaxStockProductsProvider maxStockProvider;

    @PostMapping
    public Mono<ResponseEntity<FranchiseDto>> createFranchise(@Valid @RequestBody Mono<CreateFranchiseRequest> requestMono) {
        return requestMono.flatMap(request -> {

            if (request.getName() == null) {
                log.warn("Solicitud de creación invalida: Franquicia sin nombre");
                return Mono.just(ResponseEntity.badRequest().<FranchiseDto>build());
            }

            log.info("Creando la franquicia con nombre {}", request.getName());

            return franchiseCreator.createNewFranchise(request)
                    .doOnNext(franchiseDto ->
                            log.info("La franquicia con nombre {} se creó correctamente", franchiseDto.getName())
                    )
                    .map(franchiseDto ->
                            ResponseEntity.status(HttpStatus.CREATED).body(franchiseDto)
                    );
        });
    }

    @PostMapping("/branch/add")
    public Mono<ResponseEntity<BranchDto>> addBranch(@Valid @RequestBody Mono<AddBranchRequest> requestMono) {
        return requestMono.flatMap(request -> {

            if (request.getName() == null || request.getFranchiseId() == null) {
                log.warn("Solicitud de creación de sucursal invalida");
                return Mono.just(ResponseEntity.badRequest().build());
            }

            log.info("Creando la sucursal con nombre {}", request.getName());

            return branchToFranchiseHelper.addNewBranchToFranchise(request)
                    .doOnNext(branchDto ->
                            log.info("La sucursal con nombre {} se creó correctamente", branchDto.getName())
                    )
                    .map(branchDto ->
                            ResponseEntity.status(HttpStatus.CREATED).body(branchDto)
                    );
        });
    }

    @GetMapping("/{id}/max-stock")
    public Flux<MaxStockProductDto> getMaxStockProducts(@PathVariable Long id) {

        log.info("Obteniendo los productos con mayor stock por sucursal para la franquicia {}", id);

        return maxStockProvider.get(id)
                .doOnComplete(() ->
                        log.info("Consulta completada para franquicia {}", id));
    }

    @PatchMapping("/{id}/name")
    public Mono<ResponseEntity<FranchiseDto>> updateName(
            @PathVariable Long id,
            @RequestBody @Valid UpdateFranchiseNameRequest request) {

        log.info("Actualizando el nombre de la franquicia {} a {}", id, request.getName());

        return franchiseUpdater.updateName(id, request)
                .map(ResponseEntity::ok);
    }
}