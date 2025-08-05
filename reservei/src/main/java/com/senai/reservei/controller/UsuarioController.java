package com.senai.reservei.controller;

import com.senai.reservei.dto.UsuarioCreateDTO;
import com.senai.reservei.model.Usuario;
import com.senai.reservei.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/hospede")
    public Usuario criarHospede(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO){
        return usuarioService.criarUsuario(usuarioCreateDTO, "HOSPEDE");
    }

    @PostMapping("/")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        String token = usuarioService.login(usuario);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
