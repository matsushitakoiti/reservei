package com.senai.reservei.service;

import com.senai.reservei.exception.*;
import com.senai.reservei.model.Reserva;
import com.senai.reservei.model.StatusQuartoEnum;
import com.senai.reservei.model.StatusReservaEnum;
import com.senai.reservei.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final HospedeService hospedeService;
    private final QuartoService quartoService;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, HospedeService hospedeService, QuartoService quartoService) {
        this.reservaRepository = reservaRepository;
        this.hospedeService = hospedeService;
        this.quartoService = quartoService;
    }

    public ResponseEntity criarReserva(Reserva reserva) {
        Date dataAtual = new Date();
        if (dataAtual.after(reserva.getDataEntrada())) {
            throw new ValidacaoException("Data de entrada deve ser posterior a data de atual");
        }

        if (reserva.getDataSaida().before(reserva.getDataEntrada())) {
            throw new ValidacaoException("Data de saida deve ser posterior a data de entrada");
        }

        hospedeService.buscarHospedes(reserva.getHospede().getId());
        quartoService.buscarQuarto(reserva.getQuarto().getId());

        boolean reservaExistente = reservaRepository.existsByDataEntradaLessThanEqualAndDataSaidaGreaterThanEqualAndStatusAndQuartoId(reserva.getDataSaida(), reserva.getDataEntrada(), StatusReservaEnum.ATIVA, reserva.getQuarto().getId());

        if (reservaExistente) {
            return new ResponseEntity<>("Data não disponivel", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(reservaRepository.save(reserva), HttpStatus.CREATED);
    }

    public List<Reserva> buscarReservaHospede(String documento) {
        List<Reserva> reservas = reservaRepository.findAllByHospedeDocumentoAndStatus(documento, StatusReservaEnum.ATIVA);
        if (reservas.isEmpty()) {
            throw new ReservaNaoEncontradaException(documento);
        }
        return reservas;
    }

    public Reserva buscarReserva(Long id) {
        return reservaRepository.findById(id).orElseThrow(ReservaNaoEncontradaException::new
        );
    }

    public Reserva fazerCheckin(Long id) {
        Reserva reserva = buscarReserva(id);
        reserva.getQuarto().setStatus(StatusQuartoEnum.OCUPADO);
        return reservaRepository.save(reserva);
    }

    public Reserva fazerCheckout(Long id) {
        Reserva reserva = buscarReserva(id);

        if(reserva.getQuarto().getStatus() != StatusQuartoEnum.OCUPADO) {
            new ResponseEntity<>("Checkou só pode ser realizado apos o Checkin", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Date dataAtual = new Date();
        if(dataAtual.before(reserva.getDataSaida())) {
            new ResponseEntity<>("Checkout só pode ser feito a partir da data " + reserva.getDataSaida(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        reserva.setStatus(StatusReservaEnum.CONCLUIDA);
        reserva.getQuarto().setStatus(StatusQuartoEnum.DISPONIVEL);
        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long id) {
        Reserva reserva = buscarReserva(id);
        reserva.setStatus(StatusReservaEnum.CANCELADA);
        return reservaRepository.save(reserva);
    }

}
