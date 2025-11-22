package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.franchise.dto.FranchiseDto;
import com.sonny.franchise_app.franchise.entity.Franchise;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.UpdateNameRequest;
import com.sonny.franchise_app.franchise.stub.FranchiseTestStub;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FranchiseUpdaterTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUpdater franchiseUpdater;

    @Test
    void whenUpdateStockWithProductExistsThenUpdateAndReturnDto() {

        Long id = 1L;

        UpdateNameRequest request = new UpdateNameRequest("new name");

        Franchise existingFranchise = FranchiseTestStub.getFranchiseWithNameAndId(id, "be name ");

        Franchise updatedFranchise = FranchiseTestStub.
                getFranchiseWithNameAndId(id, request.getName());

        FranchiseDto updatedDto = FranchiseTestStub.getFranchiseDto();

        updatedDto.setName(request.getName());


        when(franchiseRepository.findById(id))
                .thenReturn(Mono.just(existingFranchise));

        when(franchiseRepository.save(existingFranchise))
                .thenReturn(Mono.just(updatedFranchise));

        Mono<FranchiseDto> result = franchiseUpdater.updateName(id, request);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getId().equals(id) &&
                                dto.getName() == request.getName())
                .verifyComplete();
    }

    @Test
    void whenUpdateStockWithProductNotFoundThenThrowException() {
        Long id = 99L;

        UpdateNameRequest request = new UpdateNameRequest("new name");

        when(franchiseRepository.findById(id))
                .thenReturn(Mono.empty());

        Mono<FranchiseDto> result = franchiseUpdater.updateName(id, request);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(franchiseRepository).findById(id);
        verify(franchiseRepository, never()).save(any());
    }
}
