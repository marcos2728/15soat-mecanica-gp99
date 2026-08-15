package com.postech.mecanica.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.mecanica.model.VeiculoDTO;
import com.postech.mecanica.service.VeiculoService;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public VeiculoDTO criar(@RequestBody VeiculoDTO veiculoDTO) {
        return veiculoService.salvar(veiculoDTO);
    }

    @GetMapping
    public List<VeiculoDTO> listar() {
        return veiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public VeiculoDTO obterPorId(@PathVariable Long id) {
        return veiculoService.veiculoPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Long id, @RequestBody VeiculoDTO veiculoDTO) {
        veiculoService.atualizar(id, veiculoDTO);
        return ResponseEntity.ok("Veículo atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veiculoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
