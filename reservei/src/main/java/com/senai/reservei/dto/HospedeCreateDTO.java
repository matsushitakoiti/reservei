package com.senai.reservei.dto;

import jakarta.validation.constraints.*;

public class HospedeCreateDTO {
    @NotNull(message = "Nome nã pode ser vazio")
    @Max(value = 50, message = "Nome não pode ter mais que 50 caracteres")
    private String nome;
    @NotNull(message = "Documento não pode ser vazio")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido")
    private String documento;
    @NotNull(message = "Telefone não pode ser vazio")
    @Pattern(regexp = "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}$", message = "Número de telefone inválido")
    private String telefone;
    @NotNull
    @Email(message = "Email inválido")
    private String email;
}
