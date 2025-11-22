package com.sonny.franchise_app.branch.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AddProductRequest {

    @NotBlank(message = "Debe escribir el nombre de la sucursal.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$", message = "El nombre solo puede contener letras y espacios")
    private String name;

    @NotNull(message = "Debe escribir el stock del producto")
    @Min(value = 0, message = "El stock debe ser mayor o igual que cero")
    private int stock;
}
