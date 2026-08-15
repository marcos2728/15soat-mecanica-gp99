package com.postech.mecanica.model;

import java.time.LocalDateTime;
import java.util.List;

import com.postech.mecanica.entity.StatusOrdemServico;

public class OrdemServicoResponseDTO {

    private Long id;
    private Long veiculoId;
    private String placaVeiculo;
    private StatusOrdemServico status;
    private List<ItemEstoqueResponseDTO> pecas;
    private List<ItemServicoResponseDTO> servicos;
    private Double valorTotal;
    private LocalDateTime dataHoraCriacao;
    private LocalDateTime dataHoraAtualizacao;

    public OrdemServicoResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public void setStatus(StatusOrdemServico status) {
        this.status = status;
    }

    public List<ItemEstoqueResponseDTO> getPecas() {
        return pecas;
    }

    public void setPecas(List<ItemEstoqueResponseDTO> pecas) {
        this.pecas = pecas;
    }

    public List<ItemServicoResponseDTO> getServicos() {
        return servicos;
    }

    public void setServicos(List<ItemServicoResponseDTO> servicos) {
        this.servicos = servicos;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataHoraCriacao() {
        return dataHoraCriacao;
    }

    public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
        this.dataHoraCriacao = dataHoraCriacao;
    }

    public LocalDateTime getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
        this.dataHoraAtualizacao = dataHoraAtualizacao;
    }

    
}