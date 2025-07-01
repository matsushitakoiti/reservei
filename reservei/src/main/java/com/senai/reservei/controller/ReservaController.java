package com.senai.reservei.controller;

import com.senai.reservei.model.Reserva;
import com.senai.reservei.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public Reserva criarReserva(@RequestBody Reserva reserva) {
        return reservaService.criarReserva(reserva);
    }

    @GetMapping("/hospede")
    public List<Reserva> buscarReservaHospede(@RequestParam String documento) { return reservaService.buscarReservaHospede(documento); }

    @PutMapping("/checkin/{id}")
    public Reserva fazerCheckin(@PathVariable Long id) {
        return reservaService.fazerCheckin(id);
    }

    @PutMapping("/checkout/{id}")
    public Reserva fazerCheckout(@PathVariable Long id) {
        return reservaService.fazerCheckout(id);
    }

    @PutMapping("/cancelar/{id}")
    public Reserva cancelarReserva(@PathVariable Long id) { return reservaService.cancelarReserva(id); }
}
