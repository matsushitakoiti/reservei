package com.senai.reservei.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioCreateDTO {

    @NotBlank(message = "Usuario não pode ser vazio")
    @Size(max =50, message = "Usuario nao pode ter mais de 50 caracteres")
    public String usuario;

    @NotBlank(message = "Senha nao pode ser vazia")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String senha;

    @NotBlank(message = "Tipo não pode ser vazio")
    private String nome;

    @NotBlank(message = "Documento não pode ser vazio")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido")
    private String documento;

    @NotBlank(message = "Telefone não pode ser vazio")
    @Pattern(regexp = "^\\(\\d{2}\\)\\s(\\d{4,5})-\\d{4}$", message = "Telefone inválido")
    private String telefone;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    private String email;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
