package com.senai.reservei.service;

import com.senai.reservei.model.Quarto;
import com.senai.reservei.model.Reserva;
import com.senai.reservei.model.StatusQuartoEnum;
import com.senai.reservei.model.StatusReservaEnum;
import com.senai.reservei.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public Reserva criarReserva(Reserva reserva) {
        Date dataAtual = new Date();
        if (dataAtual.after(reserva.getDataEntrada())) {
            throw new RuntimeException("Data de entrada deve ser posterior a data atual.");
        }
        if (reserva.getDataSaida().before(reserva.getDataEntrada())) {
            throw new RuntimeException("Data de saida deve ser depois da data de entrada");
        }

        hospedeService.buscarHospedes(reserva.getHospede().getId());
        quartoService.buscarQuarto(reserva.getQuarto().getId());

        boolean reservaExistente = reservaRepository.
                existsByDataEntradaLessThanEqualAndDataSaidaGreaterThanEqualAndStatusAndQuartoId
                        (reserva.getDataSaida(), reserva.getDataEntrada(),
                                StatusReservaEnum.ATIVA, reserva.getQuarto().getId());

        if (reservaExistente) {
            throw new RuntimeException("Data não disponivel");
        }

        return reservaRepository.save(reserva);
    }

    public List<Reserva> buscarReservaHospede(String documento) {
        List<Reserva> reservas = reservaRepository.findAllByHospedeDocumentoAndStatus(documento, StatusReservaEnum.ATIVA);
        if (reservas.isEmpty()) {
            throw new RuntimeException("Nenhuma reserva encontrada para o documento " + documento);
        }
        return reservas;
    }

    public Reserva buscarReserva(Long id) {
        return reservaRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Reserva não econtrada")
        );
    }

    public Reserva fazerCheckin(Long id) {
        Reserva reserva = buscarReserva(id);
        reserva.getQuarto().setStatus(StatusQuartoEnum.OCUPADO);
        return reservaRepository.save(reserva);
    }

    public Reserva fazerCheckout(Long id) {
        Reserva reserva = buscarReserva(id);
        Date dataAtual = new Date();

        if(dataAtual.before(reserva.getDataSaida())) {
            throw new RuntimeException("Checkout só pode ser feito a partir da data " + reserva.getDataSaida());
        }

        if(reserva.getQuarto().getStatus() != StatusQuartoEnum.OCUPADO){
            throw new RuntimeException("Checkout só pode ser feito com quartos OCUPADOS!. Status atual do quarto: " + reserva.getStatus());
        }

        reserva.setStatus(StatusReservaEnum.CONCLUIDA);
        reserva.getQuarto().setStatus(StatusQuartoEnum.DISPONIVEL);
        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long id) {
        Reserva reserva = buscarReserva(id);

        reserva.setStatus(StatusReservaEnum.CANCELADA);
//        reserva.getQuarto().setStatus(StatusQuartoEnum.DISPONIVEL);
        return reservaRepository.save(reserva);
    }

}
