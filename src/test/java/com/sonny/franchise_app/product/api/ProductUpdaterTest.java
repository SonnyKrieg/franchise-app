package com.sonny.franchise_app.product.api;

import com.sonny.franchise_app.product.dto.ProductDto;
import com.sonny.franchise_app.product.entity.Product;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import com.sonny.franchise_app.product.repository.ProductRepository;
import com.sonny.franchise_app.product.request.UpdateStockRequest;
import com.sonny.franchise_app.product.stub.ProductTestStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class ProductUpdaterTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUpdater productUpdater;


    @Test
    void whenUpdateStockWithProductExistsThenUpdateAndReturnDto() {

        Long id = 1L;

        UpdateStockRequest request = new UpdateStockRequest(20);

        Product existingProduct = ProductTestStub.getDefaultProduct();
        Product updatedProduct = ProductTestStub.
                getProductWithIdAndStock(existingProduct.getId(), request.getNewStock());

        ProductDto updatedDto = ProductTestStub.getProductDto();
        updatedDto.setStock(request.getNewStock());


        when(productRepository.findById(id))
                .thenReturn(Mono.just(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(Mono.just(updatedProduct));

        Mono<ProductDto> result = productUpdater.updateStock(id, request);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.getId().equals(id) &&
                                dto.getStock() == request.getNewStock())
                .verifyComplete();
    }

    @Test
    void whenUpdateStockWithProductNotFoundThenThrowException() {
        Long id = 99L;

        UpdateStockRequest request = new UpdateStockRequest(20);

        when(productRepository.findById(id))
                .thenReturn(Mono.empty());

        Mono<ProductDto> result = productUpdater.updateStock(id, request);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any());
    }
}
