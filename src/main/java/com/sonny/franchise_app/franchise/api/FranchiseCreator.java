package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.entity.Franchise;
import com.sonny.franchise_app.franchise.exception.FranchiseDuplicateNameException;
import com.sonny.franchise_app.franchise.mapper.FranchiseMapper;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.CreateFranchiseRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class FranchiseCreator {

    private final FranchiseRepository franchiseRepository;

    public Mono<FranchiseDto> createNewFranchise(CreateFranchiseRequest request) {

        return franchiseRepository.existsByName(request.getName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(
                                new FranchiseDuplicateNameException(request.getName()));
                    }

                    Franchise franchise = Franchise.builder()
                            .name(request.getName())
                            .build();

                    return franchiseRepository.save(franchise);

                })
                .map(FranchiseMapper::toDto)
                .doOnSuccess(f -> log.info("La franquicia se guardó correctamente en la base de datos."))
                .doOnError(e -> log.error("Error al guardar franquicia", e));
    }

}
