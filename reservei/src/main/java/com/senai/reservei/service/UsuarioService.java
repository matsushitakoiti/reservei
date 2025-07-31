package com.senai.reservei.service;

import com.senai.reservei.dto.HospedeCreateDTO;
import com.senai.reservei.dto.UsuarioCreateDTO;
import com.senai.reservei.model.Usuario;
import com.senai.reservei.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final HospedeService hospedeService;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, HospedeService hospedeService) {
        this.usuarioRepository = usuarioRepository;
        this.hospedeService = hospedeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuario(username).orElseThrow(() -> new RuntimeException("Credencial não encontrada"));
    }

    public Usuario criarUsuario(UsuarioCreateDTO usuarioDTO, String tipo) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario);
        Usuario novoUsuario = usuarioRepository.save(usuario);

        if(Objects.equals(tipo, "HOSPEDE")){
            HospedeCreateDTO hospedeCreateDTO = new HospedeCreateDTO();
            BeanUtils.copyProperties(usuarioDTO, hospedeCreateDTO);
            hospedeCreateDTO.setUsuario(novoUsuario.getId());
            hospedeService.criarHospede(hospedeCreateDTO);
        } else if(Objects.equals(tipo, "ADMIN")) {
            // implementar logica
        }
        return novoUsuario;
    }
}
