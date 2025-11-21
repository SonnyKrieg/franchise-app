package com.sonny.franchise_app.branch.api;

import com.sonny.franchise_app.branch.exception.BranchNotFoundException;
import com.sonny.franchise_app.branch.repository.BranchRepository;
import com.sonny.franchise_app.branch.request.AddProductRequest;
import com.sonny.franchise_app.product.entity.Product;
import com.sonny.franchise_app.product.exception.ProductDuplicateNameException;
import com.sonny.franchise_app.product.repository.ProductRepository;
import com.sonny.franchise_app.product.stub.ProductTestStub;
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
public class AddProductToBranchHelperTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private AddProductToBranchHelper helper;

    @Test
    void addNewProductWithBranchNotExist() {

        AddProductRequest request = ProductTestStub.getDefaultProductRequest();

        when(branchRepository.existsById(request.getBranchId()))
                .thenReturn(Mono.just(false)); //

        StepVerifier.create(helper.addNewProductToBranch(request))
                .expectErrorMatches(error ->
                        error instanceof BranchNotFoundException &&
                                error.getMessage().contains(String.valueOf(request.getBranchId()))
                )
                .verify();
    }

    @Test
    void addNewProductWithProductNameExists() {

        AddProductRequest request = ProductTestStub.getDefaultProductRequest();
        when(branchRepository.existsById(request.getBranchId()))
                .thenReturn(Mono.just(true));

        when(productRepository.existsByNameAndBranchId(request.getName(), request.getBranchId()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(helper.addNewProductToBranch(request))
                .expectErrorMatches(error ->
                        error instanceof ProductDuplicateNameException &&
                                error.getMessage().contains(request.getName())
                )
                .verify();
    }

    @Test
    void addNewProductValid() {

        AddProductRequest request = ProductTestStub.getDefaultProductRequest();

        Product saved = ProductTestStub.getDefaultProduct();

        when(branchRepository.existsById(request.getBranchId()))
                .thenReturn(Mono.just(true));

        when(productRepository.existsByNameAndBranchId(request.getName(), request.getBranchId()))
                .thenReturn(Mono.just(false));

        when(productRepository.save(any(Product.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(helper.addNewProductToBranch(request))
                .assertNext(dto -> {
                    assertEquals(saved.getId(), dto.getId());
                    assertEquals(saved.getName(), dto.getName());
                    assertEquals(saved.getBranchId(), dto.getBranchId());
                    assertEquals(saved.getStock(), dto.getStock());
                })
                .verifyComplete();
    }

}
