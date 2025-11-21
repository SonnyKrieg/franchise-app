package com.sonny.franchise_app.product.exception.handler;

import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.utils.ErrorResponse;
import com.sonny.franchise_app.product.endpoint.ProductEndpoint;
import com.sonny.franchise_app.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice(assignableTypes = { ProductEndpoint.class })
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleProductNotFoundException(ProductNotFoundException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .messages(List.of(ex.getMessage()))
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now().toString())
                .error("No se encontro.")
                .build();

        return Mono.just(ResponseEntity.badRequest().body(error));
    }
}
