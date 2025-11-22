package com.sonny.franchise_app.branch.api;

import com.sonny.franchise_app.branch.dto.BranchDto;
import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.branch.exception.BranchNotFoundException;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.request.UpdateBranchNameRequest;
import com.sonny.franchise_app.branch.stub.BranchTestStub;
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
public class BranchUpdaterTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchUpdater branchUpdater;

    @Test
    void whenUpdateStockWithProductExistsThenUpdateAndReturnDto() {

        Long id = 1L;

        UpdateBranchNameRequest request = new UpdateBranchNameRequest("new name");

        Branch existingBranch = BranchTestStub.getBranchWithNameAndId( "be name ", id);

        Branch updatedBranch = BranchTestStub.
                getBranchWithNameAndId(request.getName(), id);

        BranchDto updatedDto = BranchTestStub.getBranchDto();

        updatedDto.setName(request.getName());


        when(branchRepository.findById(id))
                .thenReturn(Mono.just(existingBranch));

        when(branchRepository.save(existingBranch))
                .thenReturn(Mono.just(updatedBranch));

        Mono<BranchDto> result = branchUpdater.updateName(id, request);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getId().equals(id) &&
                                dto.getName().equals(request.getName()))
                .verifyComplete();
    }

    @Test
    void whenUpdateStockWithProductNotFoundThenThrowException() {
        Long id = 99L;

        UpdateBranchNameRequest request = new UpdateBranchNameRequest("new name");

        when(branchRepository.findById(id))
                .thenReturn(Mono.empty());

        Mono<BranchDto> result = branchUpdater.updateName(id, request);

        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();

        verify(branchRepository).findById(id);
        verify(branchRepository, never()).save(any());
    }
}
