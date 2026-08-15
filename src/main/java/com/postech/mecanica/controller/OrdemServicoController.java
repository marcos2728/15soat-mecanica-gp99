package com.postech.mecanica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postech.mecanica.model.AtualizarStatusRequestDTO;
import com.postech.mecanica.model.DiagnosticoConcluidoRequestDTO;
import com.postech.mecanica.model.OrdemServicoRequestDTO;
import com.postech.mecanica.model.OrdemServicoResponseDTO;
import com.postech.mecanica.model.TempoMedioPorFaseResponseDTO;
import com.postech.mecanica.model.TempoPorFaseResponseDTO;
import com.postech.mecanica.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens-servicos")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordemServicoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ordemServicoService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> criar(@RequestBody OrdemServicoRequestDTO requestDTO) {
        OrdemServicoResponseDTO response = ordemServicoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponseDTO> atualizarStatus(@PathVariable Long id,
            @RequestBody AtualizarStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(ordemServicoService.atualizarStatus(id, requestDTO.getStatus()));
    }

    @PatchMapping("/{id}/diagnostico-concluido")
    public ResponseEntity<OrdemServicoResponseDTO> concluirDiagnostico(@PathVariable Long id,
            @RequestBody DiagnosticoConcluidoRequestDTO requestDTO) {
        return ResponseEntity.ok(ordemServicoService.concluirDiagnostico(id, requestDTO));
    }

    @GetMapping("/{id}/tempos")
    public ResponseEntity<List<TempoPorFaseResponseDTO>> buscarTemposPorOrdemServico(@PathVariable Long id) {
        return ResponseEntity.ok(ordemServicoService.buscarTemposPorOrdemServico(id));
    }

    @GetMapping("/metricas/tempo-medio-por-fase")
    public ResponseEntity<List<TempoMedioPorFaseResponseDTO>> tempoMedioPorFase() {
        return ResponseEntity.ok(ordemServicoService.calcularTempoMedioPorFase());
    }
}