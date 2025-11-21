package com.sonny.franchise_app.franchise.exception.handler;

import com.sonny.franchise_app.branch.exception.BranchDuplicatedNameException;
import com.sonny.franchise_app.franchise.endpoint.FranchiseEndpoint;
import com.sonny.franchise_app.franchise.exception.FranchiseDuplicateNameException;
import com.sonny.franchise_app.franchise.exception.FranchiseNotFoundException;
import com.sonny.franchise_app.franchise.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.List;

@RestControllerAdvice(assignableTypes = { FranchiseEndpoint.class })
public class FranchiseExceptionHandler {

    @ExceptionHandler(FranchiseDuplicateNameException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFranchiseDuplicatedNameException(FranchiseDuplicateNameException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .messages(List.of(ex.getMessage()))
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .error("Solicitud invalida.")
                .build();

        return Mono.just(ResponseEntity.badRequest().body(error));
    }

    @ExceptionHandler(FranchiseNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFranchiseNotFoundException(FranchiseNotFoundException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .messages(List.of(ex.getMessage()))
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now().toString())
                .error("No se encontro.")
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(BranchDuplicatedNameException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBranchDuplicatedNameException(BranchDuplicatedNameException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .messages(List.of(ex.getMessage()))
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .error("Solicitud invalida.")
                .build();

        return Mono.just(ResponseEntity.badRequest().body(error));
    }
}
