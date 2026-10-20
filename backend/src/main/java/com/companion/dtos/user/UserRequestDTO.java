package com.companion.dtos.user;

import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String password,

        @NotBlank(message = "A matrícula é obrigatória")
        @Size(max = 30, message = "A matrícula deve ter no máximo 30 caracteres")
        String registrationNumber,

        @NotBlank(message = "O curso é obrigatório")
        @Size(max = 120, message = "O curso deve ter no máximo 120 caracteres")
        String course,

        @NotNull(message = "O semestre é obrigatório")
        @Min(value = 1, message = "O semestre deve ser maior ou igual a 1")
        @Max(value = 20, message = "O semestre deve ser menor ou igual a 20")
        Integer semester,

        @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
        String bio
) {
}
