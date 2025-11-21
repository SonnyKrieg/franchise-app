package com.sonny.franchise_app.franchise.endpoint;

import com.sonny.franchise_app.franchise.api.FranchiseCreator;
import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.request.CreateFranchiseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = FranchiseEndpoint.FRANCHISE_URL)
public class FranchiseEndpoint {

    public static final String FRANCHISE_URL = "/api/v1/franchise";

    private final FranchiseCreator franchiseCreator;

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

}
