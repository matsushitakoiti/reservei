package com.senai.reservei.service;

import com.senai.reservei.dto.HospedeCreateDTO;
import com.senai.reservei.dto.UsuarioCreateDTO;
import com.senai.reservei.model.RoleEnum;
import com.senai.reservei.model.Usuario;
import com.senai.reservei.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final HospedeService hospedeService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    @Lazy
    public UsuarioService(UsuarioRepository usuarioRepository, HospedeService hospedeService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.hospedeService = hospedeService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuario(username).orElseThrow(() -> new RuntimeException("Credencial não encontrada"));
    }

    public Usuario criarUsuario(UsuarioCreateDTO usuarioDTO, String tipo) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);

        Usuario novoUsuario = usuarioRepository.save(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if(Objects.equals(tipo, "HOSPEDE")){
            usuario.setRole(RoleEnum.ROLE_HOSPEDE);
            novoUsuario = usuarioRepository.save(usuario);

            HospedeCreateDTO hospedeCreateDTO = new HospedeCreateDTO();
            BeanUtils.copyProperties(usuarioDTO, hospedeCreateDTO);
            hospedeCreateDTO.setUsuario(novoUsuario.getId());
            hospedeService.criarHospede(hospedeCreateDTO);
        } else if(Objects.equals(tipo, "ADMIN")) {
            // implementar logica
        }
        return novoUsuario;
    }

    public String login(Usuario usuario) {
        UsernamePasswordAuthenticationToken usuarioSenha = new UsernamePasswordAuthenticationToken(usuario.getUsuario(), usuario.getSenha());
        Authentication auth = authenticationManager.authenticate(usuarioSenha);
        return tokenService.generatedToken((Usuario) auth.getPrincipal());
    }
}
