package com.sonny.franchise_app.franchise.api;

import com.sonny.franchise_app.branch.entity.Branch;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.stub.BranchTestStub;
import com.sonny.franchise_app.franchise.dto.MaxStockProductDto;
import com.sonny.franchise_app.franchise.entity.Franchise;
import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.repository.FranchiseRepository;
import com.sonny.franchise_app.franchise.stub.FranchiseTestStub;
import com.sonny.franchise_app.product.entity.Product;
import com.sonny.franchise_app.product.repository.ProductRepository;
import com.sonny.franchise_app.product.stub.ProductTestStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class MaxStockProductsProviderTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MaxStockProductsProvider provider;



    @Test
    public void whenFranchiseExistsThenReturnListOfMaxStockProducts() {

        Long franchiseId = 1L;

        Franchise f = FranchiseTestStub.of(franchiseId, "Franchise 1");

        Branch b1 = BranchTestStub.of(1L, "Branch A", franchiseId);
        Branch b2 = BranchTestStub.of(2L, "Branch B", franchiseId);
        Branch b3 = BranchTestStub.of(3L, "Branch C", franchiseId);

        Product pa1 = ProductTestStub.of(1L, "Prod A1", 1L, 14);
        Product pa2 = ProductTestStub.of(2L, "Prod A2", 1L, 55); // Max stock
        Product pa3 = ProductTestStub.of(3L, "Prod A3", 1L, 5);

        Product pb1 = ProductTestStub.of(4L, "Prod B1", 2L, 13);
        Product pb2 = ProductTestStub.of(5L, "Prod B2", 2L, 66);
        Product pb3 = ProductTestStub.of(6L, "Prod B3", 2L, 14);

        Product pc1 = ProductTestStub.of(7L, "Prod C1", 3L, 88);
        Product pc2 = ProductTestStub.of(8L, "Prod C2", 3L, 1111);
        Product pc3 = ProductTestStub.of(9L, "Prod C3", 3L, 11);

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(f));

        when(branchRepository.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(b1, b2, b3));

        when(productRepository.findByBranchId(1L))
                .thenReturn(Flux.just(pa1, pa2, pa3));

        when(productRepository.findByBranchId(2L))
                .thenReturn(Flux.just(pb1, pb2, pb3));

        when(productRepository.findByBranchId(3L))
                .thenReturn(Flux.just(pc1, pc2, pc3));

        Flux<MaxStockProductDto> result = provider.get(franchiseId)
                .sort(Comparator.comparing(MaxStockProductDto::getBranchId));


        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getBranchId().equals(1L)
                                && dto.getProductId().equals(2L)
                                && dto.getProductStock() == 55
                )
                .expectNextMatches(dto ->
                        dto.getBranchId().equals(2L)
                                && dto.getProductId().equals(5L)
                                && dto.getProductStock() == 66
                )
                .expectNextMatches(dto ->
                        dto.getBranchId().equals(3L)
                                && dto.getProductId().equals(8L)
                                && dto.getProductStock() == 1111
                )
                .verifyComplete();
    }

    @Test
    void whenFranchiseNotFoundThenReturnError() {
        Long franchiseId = 1L;

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        StepVerifier.create(provider.get(franchiseId))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }





}
