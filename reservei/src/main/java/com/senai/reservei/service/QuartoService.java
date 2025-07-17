package com.senai.reservei.service;

import com.senai.reservei.dto.QuartoCreateDTO;
import com.senai.reservei.dto.QuartoDTO;
import com.senai.reservei.exception.QuartoNaoEncontradoException;
import com.senai.reservei.mapper.QuartoMapper;
import com.senai.reservei.model.Quarto;
import com.senai.reservei.model.StatusQuartoEnum;
import com.senai.reservei.model.TipoQuartoEnum;
import com.senai.reservei.repository.QuartoRepository;
import com.senai.reservei.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuartoService {
    private final QuartoRepository quartoRepository;
    private final QuartoMapper quartoMapper;
    private final ReservaService reservaService;

    @Autowired
    @Lazy
    public QuartoService(QuartoRepository quartoRepository, QuartoMapper quartoMapper, ReservaRepository reservaRepository, ReservaService reservaService) {
        this.quartoRepository = quartoRepository;
        this.quartoMapper = quartoMapper;
        this.reservaService = reservaService;
    }

    public List<QuartoDTO> listarQuartos() {
        List<Quarto> quartos = quartoRepository.findAll();
        return quartoMapper.toListDTO(quartos);
    }

    public List<QuartoDTO> listarQuartosDisponiveis() {
        List<Quarto> quartos = quartoRepository.findAllByStatus(StatusQuartoEnum.DISPONIVEL);
        return quartoMapper.toListDTO(quartos);
    }

    public QuartoDTO criarQuarto(QuartoCreateDTO quartoDTO) {
        Quarto quarto = quartoMapper.toModel(quartoDTO);
        quarto.setStatus(StatusQuartoEnum.DISPONIVEL);
        quarto = quartoRepository.save(quarto);
        return quartoMapper.toDTO(quarto);
    }

    public Quarto buscarQuarto(Long id) {
        return quartoRepository.findById(id).orElseThrow(QuartoNaoEncontradoException::new);
    }

    public QuartoDTO buscarQuartoDTO(Long id) {
        Quarto quarto = quartoRepository.findById(id).orElseThrow(QuartoNaoEncontradoException::new);
        return quartoMapper.toDTO(quarto);
    }

    public QuartoDTO atualizarQuarto(QuartoCreateDTO novoQuarto, Long id) {
        Quarto quarto = buscarQuarto(id);
        quartoMapper.copyProperties(novoQuarto, quarto);
        return quartoMapper.toDTO(quartoRepository.save(quarto));
    }

    public void deletarQuarto(Long id) {
        buscarQuarto(id);
        quartoRepository.deleteById(id);
    }

    public List<QuartoDTO> listarQuartosComFiltro(Date dataEntrada, Date dataSaida, TipoQuartoEnum tipo) {
        List<Quarto> quartos = quartoRepository.findByTipo(tipo);
        List<QuartoDTO> quartoValidos = new ArrayList<QuartoDTO>();

        for(Quarto quarto: quartos) {
            if(!reservaService.verificarReservasPorDatas(dataEntrada, dataSaida, quarto.getId())){
                quartoValidos.add(quartoMapper.toDTO(quarto));
            }
        }
        return quartoValidos;
    }

}
