package com.sonny.franchise_app.franchise.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateFranchiseNameRequest {

    @NotBlank(message = "Debe escribir el nombre de la franquicia.")
    @Size(min = 2, max = 75, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

}
