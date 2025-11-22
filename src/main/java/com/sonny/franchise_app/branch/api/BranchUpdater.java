package com.sonny.franchise_app.branch.api;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.exception.BranchNotFoundException;
import com.sonny.franchise_app.branch.mapper.BranchMapper;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.request.UpdateBranchNameRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class BranchUpdater {

    private final BranchRepository branchRepository;

    public Mono<BranchDto> updateName(Long id, UpdateBranchNameRequest request) {

        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(id)))
                .flatMap(product -> {
                    product.setName(request.getName());
                    return branchRepository.save(product);
                })
                .map(BranchMapper::toDto)
                .doOnSuccess(p -> log.info("Nomnbre de la franquicia {} actualizado a {}", id, request.getName()))
                .doOnError(e -> log.error("Error actualizando el nombre de la franquicia {}", id, e));
    }
}