package com.senai.reservei.controller;

import com.senai.reservei.model.Hospede;
import com.senai.reservei.service.HospedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospedes")
public class HospedeController {
    private final HospedeService hospedeService;

    @Autowired
    public HospedeController(HospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    @GetMapping
    public List<Hospede> listarHospedes(){
        return hospedeService.listarHospedes();
    }

    @PostMapping
    public Hospede criarHospede(@RequestBody Hospede hospede) {
        return hospedeService.criarHospede(hospede);
    }

    @GetMapping("/{id}")
    public Hospede buscarHospede(@PathVariable Long id) {
        return hospedeService.buscarHospedes(id);
    }

    @PutMapping("/{id}")
    public Hospede atualizarHospede(@RequestBody Hospede novoHospede, @PathVariable Long id) {
        return hospedeService.atualizarHospedes(novoHospede, id);
    }

    @DeleteMapping("/{id}")
    public void deletarHospede(@PathVariable Long id) {
        hospedeService.deletarHospedes(id);
    }
}
