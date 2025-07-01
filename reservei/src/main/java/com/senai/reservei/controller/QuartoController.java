package com.senai.reservei.controller;

import com.senai.reservei.model.Quarto;
import com.senai.reservei.service.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    @Autowired
    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @GetMapping
    public List<Quarto> listarQuartos() {
        return quartoService.listarQuartos();
    }

    @GetMapping("/disponiveis")
    public List<Quarto> listarQuartosDisponiveis() {
        return quartoService.listarQuartosDisponiveis();
    }

    @PostMapping
    public Quarto criarQuarto(@RequestBody Quarto quarto) {
        return quartoService.criarQuarto(quarto);
    }

    @GetMapping("/{id}")
    public Quarto buscarQuarto(@PathVariable Long id) {
        return quartoService.buscarQuarto(id);
    }

    @PutMapping("/{id}")
    public Quarto atualizarQuarto(@RequestBody Quarto quarto, @PathVariable Long id) {
        return quartoService.atualizarQuarto(quarto, id);
    }

    @DeleteMapping("/{id}")
    public void deletarQuarto(@PathVariable Long id) {
        quartoService.deletarQuarto(id);
    }
}
