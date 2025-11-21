package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.branch.exception.BranchDuplicatedNameException;
import com.sonny.franchise_app.branch.mapper.BranchMapper;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.AddBranchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@AllArgsConstructor
@Service
public class AddBranchToFranchiseHelper {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<BranchDto> addNewBranchToFranchise(AddBranchRequest request) {

        return franchiseRepository.existsById(request.getFranchiseId())
                .flatMap(existsFranchise -> {
                    if (!existsFranchise)
                        return Mono.error(new FranchiseNotFoundException(request.getFranchiseId()));
                    return branchRepository.existsByName(request.getName());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(
                                new BranchDuplicatedNameException(request.getName()));
                    }

                    Branch branch = Branch.builder()
                            .name(request.getName())
                            .franchiseId(request.getFranchiseId())
                            .build();
                    return branchRepository.save(branch);
                })
                .map(BranchMapper::toDto)
                .doOnSuccess(f -> log.info("La sucursal se guardó correctamente en la base de datos."))
                .doOnError(e -> log.error("Error al guardar la sucursal", e));
    }

}
