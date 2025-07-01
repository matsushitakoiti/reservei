package com.senai.reservei.service;

import com.senai.reservei.model.Quarto;
import com.senai.reservei.model.StatusQuartoEnum;
import com.senai.reservei.repository.QuartoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService {
    private final QuartoRepository quartoRepository;

    @Autowired
    public QuartoService(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    public List<Quarto> listarQuartos() {
        return quartoRepository.findAll();
    }

    public List<Quarto> listarQuartosDisponiveis() {
        return quartoRepository.findAllByStatus(StatusQuartoEnum.DISPONIVEL);
    }

    public Quarto criarQuarto(Quarto quarto) {
        return quartoRepository.save(quarto);
    }

    public Quarto buscarQuarto(Long id) {
        return quartoRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Quarto não encontrado")
        );
    }

    public Quarto atualizarQuarto(Quarto novoQuarto, Long id) {
        Quarto quarto = quartoRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Quarto não encontrado")
        );
        BeanUtils.copyProperties(novoQuarto, quarto);
        return quartoRepository.save(quarto);
    }

    public void deletarQuarto(Long id) {
        quartoRepository.deleteById(id);
    }

}
