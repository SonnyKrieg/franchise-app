package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.branch.exception.BranchDuplicatedNameException;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.stub.BranchTestStub;
import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.request.AddBranchRequest;
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
public class AddBranchToFranchiseHelperTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private AddBranchToFranchiseHelper helper;

    @Test
    void whenAddNewBranchWithFranchiseNotExistThenThrowsException() {

        AddBranchRequest request = BranchTestStub.getDefaultBranchRequest();
        Long franchiseId = 1L;

        when(franchiseRepository.existsById(franchiseId))
                .thenReturn(Mono.just(false)); //

        StepVerifier.create(helper.addNewBranchToFranchise(franchiseId, request))
                .expectErrorMatches(error ->
                        error instanceof FranchiseNotFoundException &&
                                error.getMessage().contains(String.valueOf(franchiseId))
                )
                .verify();
    }

    @Test
    void whenAddNewBranchWithBranchNameExistsThenThrowException() {

        AddBranchRequest request = BranchTestStub.getDefaultBranchRequest();
        Long franchiseId = 1L;

        when(franchiseRepository.existsById(franchiseId))
                .thenReturn(Mono.just(true));

        when(branchRepository.existsByName(request.getName()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(helper.addNewBranchToFranchise(franchiseId, request))
                .expectErrorMatches(error ->
                        error instanceof BranchDuplicatedNameException &&
                                error.getMessage().contains(request.getName())
                )
                .verify();
    }

    @Test
    void whenAddNewBranchWithValidThenSaveAndReturnDto() {

        AddBranchRequest request = BranchTestStub.getDefaultBranchRequest();
        Long franchiseId = 1L;

        Branch saved = BranchTestStub.getDefaultBranch();

        when(franchiseRepository.existsById(franchiseId))
                .thenReturn(Mono.just(true));

        when(branchRepository.existsByName(request.getName()))
                .thenReturn(Mono.just(false));

        when(branchRepository.save(any(Branch.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(helper.addNewBranchToFranchise(franchiseId, request))
                .assertNext(dto -> {
                    assertEquals(saved.getId(), dto.getId());
                    assertEquals(saved.getName(), dto.getName());
                    assertEquals(saved.getFranchiseId(), dto.getFranchiseId());
                })
                .verifyComplete();
    }

}
