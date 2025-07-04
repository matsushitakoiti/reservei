package com.senai.reservei.service;

import com.senai.reservei.exception.HospedeNaoEncontradoException;
import com.senai.reservei.model.Hospede;
import com.senai.reservei.repository.HospedeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospedeService {
    private final HospedeRepository hospedeRepository;

    @Autowired
    public HospedeService(HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
    }

    public List<Hospede> listarHospedes() {
        return hospedeRepository.findAll();
    }

    public Hospede criarHospede(Hospede hospede) {
        return hospedeRepository.save(hospede);
    }

    public Hospede buscarHospedes(Long id) {
        return hospedeRepository.findById(id).orElseThrow(
                HospedeNaoEncontradoException::new
        );
    }

    public Hospede atualizarHospedes(Hospede novoHospede, Long id) {
        Hospede hospede = buscarHospedes(id);
        BeanUtils.copyProperties(novoHospede, hospede);
        return hospedeRepository.save(hospede);
    }

    public void deletarHospedes(Long id) {
        Hospede hospede = buscarHospedes(id);
        hospedeRepository.delete(hospede);
    }
}
