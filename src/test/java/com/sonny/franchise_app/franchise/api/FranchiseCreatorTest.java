package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.franchise.entity.Franchise;
import com.sonny.franchise_app.franchise.exception.FranchiseDuplicateNameException;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.CreateFranchiseRequest;
import com.sonny.franchise_app.franchise.stub.FranchiseTestStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FranchiseCreatorTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseCreator franchiseCreator;



    @Test
    void createNewFranchiseWithDuplicatedName() {

        CreateFranchiseRequest request = FranchiseTestStub.getCreateFranchiseRequest();

        when(franchiseRepository.existsByName(request.getName()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(franchiseCreator.createNewFranchise(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof FranchiseDuplicateNameException &&
                                throwable.getMessage().contains(request.getName()))
                .verify();
    }

    @Test
    void createNewFranchiseWithNoDuplicatedName() {

        CreateFranchiseRequest request = FranchiseTestStub.getCreateFranchiseRequest();

        Franchise saved = FranchiseTestStub.getDefaultFranchise();

        when(franchiseRepository.existsByName(request.getName()))
                .thenReturn(Mono.just(false));

        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(franchiseCreator.createNewFranchise(request))
                .assertNext(dto -> {
                    assertEquals(request.getName(), dto.getName());
                    assertEquals(saved.getId(), dto.getId());
                })
                .verifyComplete();
    }

}
