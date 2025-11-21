package com.sonny.franchise_app.product.api;

import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import com.sonny.franchise_app.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductDeleterTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductDeleter productDeleter;

    @Test
    void whenDeleteProductExistsThenDeleteSuccessfully() {
        Long id = 1L;

        when(productRepository.existsById(id))
                .thenReturn(Mono.just(true));

        when(productRepository.deleteById(id))
                .thenReturn(Mono.empty());

        Mono<Void> result = productDeleter.deleteProductById(id);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productRepository).existsById(id);
        verify(productRepository).deleteById(id);
    }

    @Test
    void whenDeleteProductByIdNoExitsThenThrowNotFound() {
        Long id = 1L;

        when(productRepository.existsById(id))
                .thenReturn(Mono.just(false));

        Mono<Void> result = productDeleter.deleteProductById(id);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepository).existsById(id);
        verify(productRepository, never()).deleteById(id);
    }
}
